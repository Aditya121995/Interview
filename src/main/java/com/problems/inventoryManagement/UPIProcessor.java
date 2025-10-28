package com.problems.inventoryManagement;

public class UPIProcessor implements PaymentProcessor {
    @Override
    public PaymentStatus process() {
        System.out.println("UPI Payment is Success");
        return PaymentStatus.SUCCESS;
    }
}
