package com.problems.BookMyShow;


public class DebitCardProcessor implements PaymentProcessor {
    @Override
    public PaymentStatus process(Payment payment) {
        System.out.println("Payment processed via Debit Card. Amount :: " + payment.getAmount());
        return PaymentStatus.SUCCESS;
    }
}
