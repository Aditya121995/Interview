package com.problems.RideBookingSystem;

public class StartedState implements RideState {
    @Override
    public void accept(Ride ride, Driver driver) {
        System.out.println("Ride is already accepted and started");
    }

    @Override
    public void start(Ride ride, Driver driver) {
        System.out.println("Ride is already started");
    }

    @Override
    public void complete(Ride ride, Driver driver) {
        if (!ride.getDriver().getId().equals(driver.getId())) {
            System.out.println("Ride can only be completed by driver who accepted it, i.e. " + driver.getName());
            return;
        }
        ride.setState(new CompletedState());
        ride.setEndTime(System.currentTimeMillis());
        System.out.println("Ride" + ride.getRideId() + " is completed");
    }

    @Override
    public void cancel(Ride ride, Driver driver) {
        System.out.println("Cannot cancel ride which is already started");
    }

    @Override
    public RideStatus getStatus() {
        return RideStatus.STARTED;
    }
}
