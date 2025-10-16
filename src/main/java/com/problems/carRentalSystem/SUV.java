package com.problems.carRentalSystem;

public class SUV extends Vehicle {
    public SUV(String vehicleId, String model, String licensePlate, double pricePerHour) {
        super(vehicleId, model, licensePlate, VehicleType.SUV,  pricePerHour);
    }
}
