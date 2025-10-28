package com.problems.inventoryManagement;

public class DebitCardProcessor implements PaymentProcessor {
    @Override
    public PaymentStatus process() {
        System.out.println("Debit Card Payment is Success");
        return PaymentStatus.SUCCESS;
    }
}
