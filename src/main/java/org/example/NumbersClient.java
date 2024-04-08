package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.CountDownLatch;

import static number.NumbersGeneratorGrpc.NumbersGeneratorStub;
import static number.NumbersGeneratorGrpc.newStub;
import static number.NumbersGeneratorOuterClass.NumbersRequest;

public class NumbersClient {
    private static int currentValue = 0;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        NumbersGeneratorStub stub = newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        ClientStreamObserver observer = new ClientStreamObserver(latch);
        stub.generateNumbers(makeNumberRequest(), observer);

        int newValue;
        int beforeNumber = -1;

        for (int i = 0; i < 50; i++) {
            newValue = observer.getNewValue();

            if (beforeNumber == newValue) {
                currentValue++;
            } else {
                currentValue = currentValue + newValue + 1;
            }

            System.out.println("currentValue:" + currentValue);
            beforeNumber = newValue;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        latch.await();
        channel.shutdown();
    }

    private static NumbersRequest makeNumberRequest() {
        return NumbersRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }
}