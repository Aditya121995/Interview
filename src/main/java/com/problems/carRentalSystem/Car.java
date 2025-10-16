package com.problems.carRentalSystem;

public class Car extends Vehicle {
    public Car(String vehicleId, String model, String licensePlate, double pricePerHour) {
        super(vehicleId, model, licensePlate, VehicleType.CAR, pricePerHour);
    }
}
