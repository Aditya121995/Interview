package com.problems.RideBookingSystem;

import lombok.Getter;

@Getter
public class Vehicle {
    private final String vehicleNumber;
    private final RideType vehicleType;
    private final String model;

    public Vehicle(String vehicleNumber, RideType vehicleType, String model) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.model = model;
    }
}
