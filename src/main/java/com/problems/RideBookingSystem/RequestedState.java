package com.problems.RideBookingSystem;

public class RequestedState implements RideState {
    @Override
    public void accept(Ride ride, Driver driver) {
        if(ride.assignDriver(driver)) {
            ride.setState(new AcceptedState());
            System.out.println("Ride " + ride.getRideId() + " accepted by driver " + driver.getName());
        }

    }

    @Override
    public void start(Ride ride, Driver driver) {
        System.out.println("Cannot start ride in the REQUESTED state");
    }

    @Override
    public void complete(Ride ride, Driver driver) {
        System.out.println("Cannot complete ride in the REQUESTED state");
    }

    @Override
    public void cancel(Ride ride, Driver driver) {
        if (driver != null) {
            System.out.println("Driver cannot cancel ride in the REQUESTED state");
            return;
        }
        ride.setState(new CanceledState());
        System.out.println("Ride" + ride.getRideId() + " is canceled by passenger " + ride.getPassenger().getName());
    }

    @Override
    public RideStatus getStatus() {
        return RideStatus.REQUESTED;
    }
}
