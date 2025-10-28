package com.problems.inventoryManagement;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Payment {
    private final String paymentId;
    private final String userId;
    private final String orderId;
    private final double amount;
    @Setter
    private PaymentStatus status;
    private final PaymentMethod paymentMethod;

    public Payment(String userId, String orderId, double amount, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.paymentMethod = paymentMethod;
    }
}
