package com.problems.ParkingLotSystem;

public class PaymentStrategyCash implements PaymentStrategy {
    @Override
    public boolean processPayment(double price) {
        System.out.println("Processing Cash :: " + price);
        return true;
    }
}
