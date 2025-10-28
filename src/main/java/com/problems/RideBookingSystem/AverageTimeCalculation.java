package com.problems.RideBookingSystem;

public class AverageTimeCalculation implements TimeCalculationStrategy {
    private static final double AVERAGE_SPEED = 60.0;

    @Override
    public long calculateEstimatedRideTime(double distance, double trafficFactor) {
        double duration = distance/AVERAGE_SPEED;
        double weightedDuration = duration*trafficFactor;

        return Math.round(weightedDuration*60);
    }
}
