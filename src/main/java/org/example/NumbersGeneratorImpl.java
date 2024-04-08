package org.example;

import io.grpc.stub.StreamObserver;

import static number.NumbersGeneratorGrpc.NumbersGeneratorImplBase;
import static number.NumbersGeneratorOuterClass.NumbersRequest;
import static number.NumbersGeneratorOuterClass.NumbersResponse;

public class NumbersGeneratorImpl extends NumbersGeneratorImplBase {
    @Override
    public void generateNumbers(NumbersRequest request, StreamObserver<NumbersResponse> responseObserver) {
        int firstValue = request.getFirstValue() - 1;
        int lastValue = request.getLastValue();

        while (firstValue < lastValue) {

            firstValue++;
            responseObserver.onNext(NumbersResponse.newBuilder().setNumber(firstValue).build());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();
    }
}