package com.problems.RideBookingSystem;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

@Getter
public class Driver {
    private final String id;
    private final String name;
    private final String number;
    @Setter
    private Location currentLocation;
    private AtomicReference<DriverStatus> driverStatus;
    private final Vehicle vehicle;

    public Driver(String id, String name, String number, Vehicle vehicle, Location location) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.vehicle = vehicle;
        this.driverStatus = new AtomicReference<>(DriverStatus.AVAILABLE);
        this.currentLocation = location;
    }

    public boolean setDriverStatus(DriverStatus expectedStatus, DriverStatus newStatus) {
        return this.driverStatus.compareAndSet(expectedStatus, newStatus);
    }

    public DriverStatus getDriverStatus() {
        return this.driverStatus.get();
    }
}
