package com.problems.BookMyShow;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Payment {
    private final String paymentId;
    private final String userId;
    private final String bookingId;
    private final PaymentMethod paymentMethod;
    private final double amount;
    @Setter
    private PaymentStatus status;

    public Payment(String userId, String bookingId, double amount, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.paymentId = UUID.randomUUID().toString();
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }
}
