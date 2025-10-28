package com.problems.RideBookingSystem;

public interface TimeCalculationStrategy {
    long calculateEstimatedRideTime(double distance, double trafficFactor);
}
