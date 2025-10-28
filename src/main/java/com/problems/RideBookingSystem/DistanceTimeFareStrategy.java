package com.problems.RideBookingSystem;

public class DistanceTimeFareStrategy implements FareCalculationStrategy {
    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 15.0;
    private static final double PER_MIN_RATE = 2.0;
    private static final double MIN_FARE = 80.0;

    @Override
    public double calculateFare(Ride ride, double distance, long duration) {
        long durationInMin = duration/60;

        double baseFare = BASE_FARE;
        double distanceFare = distance*PER_KM_RATE;
        double timeFare = durationInMin*PER_MIN_RATE;

        double totalFare = baseFare + distanceFare + timeFare;

        totalFare = Math.max(MIN_FARE, totalFare);
        return (totalFare*100.0)/100.0;
    }
}
