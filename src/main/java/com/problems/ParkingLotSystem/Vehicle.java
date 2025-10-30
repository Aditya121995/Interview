package com.problems.ParkingLotSystem;

import lombok.Getter;

@Getter
public abstract class Vehicle {
    private String vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(String vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }
}
