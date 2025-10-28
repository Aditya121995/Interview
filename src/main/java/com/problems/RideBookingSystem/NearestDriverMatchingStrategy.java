package com.problems.RideBookingSystem;

import java.util.List;
import java.util.stream.Collectors;

public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
    private final static double MAX_DISTANCE = 5.0;

    @Override
    public List<Driver> findDrivers(List<Driver> driverList, Location pickupLocation, RideType rideType) {
        System.out.println("Finding nearest driver for the ride type " +  rideType);
        return driverList.stream()
                .filter(driver -> driver.getDriverStatus().equals(DriverStatus.AVAILABLE))
                .filter(driver -> driver.getVehicle().getVehicleType().equals(rideType))
                .filter(driver -> driver.getCurrentLocation().calculateDistance(pickupLocation) <= MAX_DISTANCE)
                .collect(Collectors.toList());
    }
}
