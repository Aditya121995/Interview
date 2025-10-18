package com.problems.bookMyShow;

import com.problems.carRentalSystem.PaymentMethod;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Payment {
    private final String paymentId;
    private PaymentMethod paymentMethod;
    private final BigDecimal amount;
    private PaymentStatus status;

    public Payment(BigDecimal amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.paymentMethod = null;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public boolean processPayment(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        status = PaymentStatus.SUCCESS;
        return true;
    }
}
