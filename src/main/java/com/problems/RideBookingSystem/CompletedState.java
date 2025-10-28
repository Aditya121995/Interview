package com.problems.RideBookingSystem;

public class CompletedState implements RideState {
    @Override
    public void accept(Ride ride, Driver driver) {
        System.out.println("Cannot accept a completed ride");
    }

    @Override
    public void start(Ride ride, Driver driver) {
        System.out.println("Cannot start a completed ride");
    }

    @Override
    public void complete(Ride ride, Driver driver) {
        System.out.println("Ride already completed");
    }

    @Override
    public void cancel(Ride ride, Driver driver) {
        System.out.println("Cannot cancel a completed ride");
    }

    @Override
    public RideStatus getStatus() {
        return RideStatus.COMPLETED;
    }
}
