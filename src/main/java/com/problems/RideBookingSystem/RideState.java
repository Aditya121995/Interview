package com.problems.RideBookingSystem;

public interface RideState {
    void accept(Ride ride, Driver driver);
    void start(Ride ride, Driver driver);
    void complete(Ride ride, Driver driver);
    void cancel(Ride ride, Driver driver);
    RideStatus getStatus();
}
