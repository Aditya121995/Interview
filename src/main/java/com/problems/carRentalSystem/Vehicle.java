package com.problems.carRentalSystem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public abstract class Vehicle {
    private final String vehicleId;
    private final String model;
    private final String licensePlate;
    @Setter
    private String location;
    private final VehicleType vehicleType;
    @Setter
    private VehicleStatus status;
    private final double pricePerHour;


    public Vehicle(String vehicleId, String model, String licensePlate, VehicleType vehicleType, double pricePerHour) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.pricePerHour = pricePerHour;
        this.status = VehicleStatus.AVAILABLE;
    }
}
