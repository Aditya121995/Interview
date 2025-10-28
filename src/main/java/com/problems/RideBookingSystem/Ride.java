package com.problems.RideBookingSystem;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Ride {
    private final String rideId;
    private final RideType rideType;
    private final Passenger passenger;
    private Driver driver;
    private final Location pickupLocation;
    private final Location dropOffLocation;
    @Setter
    private double fare;
    @Setter
    private long startTime;
    @Setter
    private long endTime;
    private RideState currentState;
    private final FareCalculationStrategy fareCalculationStrategy;
    private final Object stateLock = new Object();

    public Ride(RideType rideType,
                Passenger passenger,
                Location pickupLocation,
                Location dropOffLocation,
                FareCalculationStrategy fareCalculationStrategy) {

        this.rideId = UUID.randomUUID().toString();
        this.rideType = rideType;
        this.passenger = passenger;
        this.pickupLocation = pickupLocation;
        this.dropOffLocation = dropOffLocation;
        this.currentState = new RequestedState();
        this.fareCalculationStrategy = fareCalculationStrategy;
    }

    public boolean assignDriver(Driver driver) {
        synchronized (stateLock) {
            if (this.driver == null && currentState.getStatus().equals(RideStatus.REQUESTED)) {
                if(!driver.setDriverStatus(driver.getDriverStatus(), DriverStatus.BUSY)) {
                    System.out.println("Driver became unavailable");
                    return false;
                }
                this.driver = driver;
                return true;
            }
            System.out.println(this.driver != null);
            return false;
        }
    }

    public void setState(RideState state) {
        synchronized (stateLock) {
            System.out.println("Ride State transition from " + currentState.getStatus() +  " to" + state.getStatus());
            this.currentState = state;
        }
    }

    public RideStatus getRideStatus() {
        synchronized (stateLock) {
            return currentState.getStatus();
        }
    }

    public void accept(Driver driver) {
        synchronized (stateLock) {
            currentState.accept(this, driver);
        }
    }

    public void start(Driver driver) {
        synchronized (stateLock) {
            currentState.start(this, driver);
        }
    }

    public void complete(Driver driver) {
        synchronized (stateLock) {
            currentState.complete(this, driver);
        }
    }

    public void cancelByDriver(Driver driver) {
        synchronized (stateLock) {
            currentState.cancel(this, driver);
        }
    }

    public void cancelByPassenger() {
        synchronized (stateLock) {
            currentState.cancel(this, null);
        }
    }
}
