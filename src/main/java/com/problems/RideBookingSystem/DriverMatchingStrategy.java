package com.problems.RideBookingSystem;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findDrivers(List<Driver> driverList, Location pickupLocation, RideType rideType);
}
