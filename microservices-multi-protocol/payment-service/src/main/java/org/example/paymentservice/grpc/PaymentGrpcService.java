package org.example.paymentservice.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.paymentservice.dto.ProcessPaymentRequestDTO;
import org.example.paymentservice.model.Payment;
import org.example.paymentservice.model.PaymentStatus;
import org.example.paymentservice.service.PaymentService;

import java.util.List;

@GrpcService
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final PaymentService paymentService;

    public PaymentGrpcService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void processPayment(ProcessPaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {

        Payment saved = paymentService.processPayment(
                request.getOrderId(),
                request.getAmount(),
                request.getCurrency(),
                request.getPaymentMethod()
        );

        responseObserver.onNext(toGrpcResponse(saved));
        responseObserver.onCompleted();
    }


    @Override
    public void getPayment(GetPaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        Payment payment = paymentService.getPaymentById(request.getId());
        responseObserver.onNext(toGrpcResponse(payment));
        responseObserver.onCompleted();
    }

    @Override
    public void getPaymentsByOrder(GetPaymentsByOrderRequest request, StreamObserver<PaymentsResponse> responseObserver) {
        List<Payment> payments = paymentService.getPaymentsByOrderId(request.getOrderId());

        PaymentsResponse.Builder builder = PaymentsResponse.newBuilder();
        for (Payment p : payments) {
            builder.addPayments(toGrpcResponse(p));
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updatePaymentStatus(UpdatePaymentStatusRequest request, StreamObserver<PaymentResponse> responseObserver) {
        PaymentStatus status = fromGrpcStatus(request.getStatus());
        Payment updated = paymentService.updatePaymentStatus(request.getId(), status);

        responseObserver.onNext(toGrpcResponse(updated));
        responseObserver.onCompleted();
    }

    @Override
    public void refundPayment(RefundPaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        Payment refunded = paymentService.refundPayment(request.getId());

        responseObserver.onNext(toGrpcResponse(refunded));
        responseObserver.onCompleted();
    }

    // --------- Helpers ---------

    private PaymentResponse toGrpcResponse(Payment payment) {
        return PaymentResponse.newBuilder()
                .setId(payment.getId() != null ? payment.getId() : 0)
                .setOrderId(payment.getOrderId() != null ? payment.getOrderId() : 0)
                .setAmount(payment.getAmount() != null ? payment.getAmount() : 0.0)
                .setCurrency(payment.getCurrency() != null ? payment.getCurrency() : "")
                .setStatus(toGrpcStatus(payment.getStatus()))
                .setPaymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "")
                .setTransactionId(payment.getTransactionId() != null ? payment.getTransactionId() : "")
                .setCreatedAt(payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : "")
                .setUpdatedAt(payment.getUpdatedAt() != null ? payment.getUpdatedAt().toString() : "")
                .build();
    }

    private org.example.paymentservice.grpc.PaymentStatus toGrpcStatus(PaymentStatus status) {
        if (status == null) return org.example.paymentservice.grpc.PaymentStatus.PAYMENT_STATUS_UNSPECIFIED;
        return switch (status) {
            case PENDING -> org.example.paymentservice.grpc.PaymentStatus.PENDING;
            case COMPLETED -> org.example.paymentservice.grpc.PaymentStatus.COMPLETED;
            case FAILED -> org.example.paymentservice.grpc.PaymentStatus.FAILED;
            case REFUNDED -> org.example.paymentservice.grpc.PaymentStatus.REFUNDED;
        };
    }

    private PaymentStatus fromGrpcStatus(org.example.paymentservice.grpc.PaymentStatus status) {
        return switch (status) {
            case PENDING -> PaymentStatus.PENDING;
            case COMPLETED -> PaymentStatus.COMPLETED;
            case FAILED -> PaymentStatus.FAILED;
            case REFUNDED -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.PENDING;
        };
    }
}