package com.problems.RideBookingSystem;

public interface FareCalculationStrategy {
    double calculateFare(Ride ride, double distance, long duration);
}
