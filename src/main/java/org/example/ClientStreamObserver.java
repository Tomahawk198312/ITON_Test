package org.example;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

import static number.NumbersGeneratorOuterClass.NumbersResponse;

public class ClientStreamObserver implements StreamObserver<NumbersResponse> {
    private final CountDownLatch latch;
    private int newValue = 0;

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(NumbersResponse newValue) {
        this.newValue = newValue.getNumber();
        System.out.println("newValue:" + newValue.getNumber());
    }

    public int getNewValue() {
        return newValue;
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("error");
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("request completed");
        latch.countDown();
    }
}
