package com.problems.parkingLotSystem;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class ParkingSpot {
    private final String spotId;
    private final SpotType spotType;
    private final Set<VehicleType> supportedVehicleTypes;
    private Vehicle vehicle;
    private boolean isAvailable;
    private final Coordinate coordinate;

    public ParkingSpot(String spotId, SpotType spotType, Coordinate coordinate) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.supportedVehicleTypes = getSupportedVehicle();
        this.isAvailable = true;
        this.coordinate = coordinate;
    }

    private Set<VehicleType> getSupportedVehicle() {
        switch (spotType) {
            case SMALL:
                return new HashSet<>(Arrays.asList(VehicleType.BIKE));
            case MEDIUM:
                return new HashSet<>(Arrays.asList(VehicleType.BIKE,  VehicleType.CAR));
            case LARGE:
                return new HashSet<>(Arrays.asList(VehicleType.BIKE,  VehicleType.CAR,   VehicleType.TRUCK));
            default:
                return new HashSet<>();
        }
    }

    public synchronized boolean park(Vehicle vehicle) {
        if (!this.isAvailable || !canMakeParking(vehicle.getVehicleType())) {
            return false;
        }
        this.vehicle = vehicle;
        this.isAvailable = false;
        return true;
    }

    public boolean canMakeParking(VehicleType vehicleType) {
        return supportedVehicleTypes.contains(vehicleType);
    }

    public synchronized boolean unpark() {
        if (this.isAvailable) {
            return false;
        }
        this.vehicle = null;
        this.isAvailable = true;
        return true;
    }

}
