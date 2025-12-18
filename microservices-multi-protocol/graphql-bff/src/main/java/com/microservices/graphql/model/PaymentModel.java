package com.microservices.graphql.model;

import lombok.Data;

@Data
public class PaymentModel {
    private Long id;
    private Long orderId;
    private Double amount;
    private String currency;
    private String status;
    private String paymentMethod;
    private String transactionId;
    private String createdAt;
    private String updatedAt;
}