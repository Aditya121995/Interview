package com.problems.RideBookingSystem;

public class CanceledState implements RideState {
    @Override
    public void accept(Ride ride, Driver driver) {
        System.out.println("Cannot accept a cancelled ride");
    }

    @Override
    public void start(Ride ride, Driver driver) {
        System.out.println("Cannot accept a cancelled ride");
    }

    @Override
    public void complete(Ride ride, Driver driver) {
        System.out.println("Cannot accept a cancelled ride");
    }

    @Override
    public void cancel(Ride ride, Driver driver) {
        System.out.println("Ride is already cancelled");
    }

    @Override
    public RideStatus getStatus() {
        return null;
    }
}
