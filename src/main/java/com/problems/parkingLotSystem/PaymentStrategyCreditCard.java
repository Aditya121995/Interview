package com.problems.parkingLotSystem;

public class PaymentStrategyCreditCard implements PaymentStrategy {
    @Override
    public boolean processPayment(double price) {
        System.out.println("Processing Credit Card :: " + price);
        return true;
    }
}
