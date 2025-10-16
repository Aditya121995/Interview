package com.problems.carRentalSystem;

public interface PaymentProcessor {
    boolean process(Reservation reservation);
}
