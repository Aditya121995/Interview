package com.problems.ParkingLotSystem;

public class PaymentStrategyUPI implements PaymentStrategy {
    @Override
    public boolean processPayment(double price) {
        System.out.println("Payment Done via UPI :: " + price);
        return true;
    }
}
