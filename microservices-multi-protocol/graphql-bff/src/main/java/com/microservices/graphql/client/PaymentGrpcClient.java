package com.microservices.graphql.client;

import org.example.paymentservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentGrpcClient {

    @GrpcClient("payment-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub stub;

    public PaymentResponse getPayment(Long id) {
        return stub.getPayment(
                GetPaymentRequest.newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        return stub.getPaymentsByOrder(
                GetPaymentsByOrderRequest.newBuilder()
                        .setOrderId(orderId)
                        .build()
        ).getPaymentsList();
    }

    public PaymentResponse processPayment(Long orderId, double amount, String currency, String method) {
        return stub.processPayment(
                ProcessPaymentRequest.newBuilder()
                        .setOrderId(orderId)
                        .setAmount(amount)
                        .setCurrency(currency)
                        .setPaymentMethod(method)
                        .build()
        );
    }

    public PaymentResponse updatePaymentStatus(Long id, PaymentStatus status) {
        return stub.updatePaymentStatus(
                UpdatePaymentStatusRequest.newBuilder()
                        .setId(id)
                        .setStatus(status)
                        .build()
        );
    }

    public PaymentResponse refundPayment(Long id) {
        return stub.refundPayment(
                RefundPaymentRequest.newBuilder()
                        .setId(id)
                        .build()
        );
    }
}