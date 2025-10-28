package com.problems.RideBookingSystem;

public class AcceptedState implements RideState {
    @Override
    public void accept(Ride ride, Driver driver) {
        System.out.println("Ride is already accepted.");
    }

    @Override
    public void start(Ride ride,  Driver driver) {
        if (!ride.getDriver().getId().equals(driver.getId())) {
            System.out.println("Ride can only be started by driver who accepted it, i.e. " + driver.getName());
            return;
        }
        ride.setState(new StartedState());
        ride.setStartTime(System.currentTimeMillis());
        System.out.println("Ride" + ride.getRideId() + " is started");
    }

    @Override
    public void complete(Ride ride, Driver driver) {
        System.out.println("Cannot complete ride in the ACCEPTED state");
    }

    @Override
    public void cancel(Ride ride, Driver driver) {
        if (driver == null) {
            ride.setState(new CanceledState());
            System.out.println("Ride" + ride.getRideId() + " is canceled by passenger " + ride.getPassenger().getName());
        } else {
            if (!driver.getId().equals(ride.getDriver().getId())) {
                System.out.println("Ride can only be canceled by driver who accepted it, i.e. " + driver.getName());
                return;
            }

            ride.setState(new CanceledState());
            System.out.println("Ride" + ride.getRideId() + " is canceled by driver " + ride.getDriver().getName());
        }
    }

    @Override
    public RideStatus getStatus() {
        return RideStatus.ACCEPTED;
    }
}
