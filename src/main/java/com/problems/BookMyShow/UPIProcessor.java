package com.problems.BookMyShow;


public class UPIProcessor implements PaymentProcessor {
    @Override
    public PaymentStatus process(Payment payment) {
        System.out.println("Payment processed via UPI. Amount :: " + payment.getAmount());
        return PaymentStatus.SUCCESS;
    }
}
