package com.problems.ParkingLotSystem;

public class PaymentStrategyDebitCard implements PaymentStrategy {
    @Override
    public boolean processPayment(double price) {
        System.out.println("Processing Debit Card  :: " + price);
        return true;
    }
}
