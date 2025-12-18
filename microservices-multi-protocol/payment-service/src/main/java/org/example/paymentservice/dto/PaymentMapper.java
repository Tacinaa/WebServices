package org.example.paymentservice.dto;

import org.example.paymentservice.model.Payment;

public class PaymentMapper {

    private PaymentMapper() {}

    public static PaymentResponseDTO toResponse(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : null);
        dto.setUpdatedAt(payment.getUpdatedAt() != null ? payment.getUpdatedAt().toString() : null);
        return dto;
    }
}