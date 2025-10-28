package com.problems.RideBookingSystem;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RideService {
    private static RideService instance;
    private final Map<String, Ride> rideMap;
    private final Map<String, Passenger> passengerMap;
    private final Map<String, Driver> driverMap;
    private FareCalculationStrategy fareCalculationStrategy;
    private DriverMatchingStrategy driverMatchingStrategy;
    private TimeCalculationStrategy timeCalculationStrategy;

    private RideService(FareCalculationStrategy fareCalculationStrategy,
                       DriverMatchingStrategy driverMatchingStrategy,
                        TimeCalculationStrategy timeCalculationStrategy) {
        this.rideMap = new ConcurrentHashMap<>();
        this.passengerMap = new ConcurrentHashMap<>();
        this.driverMap = new ConcurrentHashMap<>();
        this.fareCalculationStrategy = fareCalculationStrategy;
        this.driverMatchingStrategy = driverMatchingStrategy;
        this.timeCalculationStrategy = timeCalculationStrategy;
    }

    public static synchronized RideService getInstance(FareCalculationStrategy fareCalculationStrategy,
                                          DriverMatchingStrategy driverMatchingStrategy,
                                                       TimeCalculationStrategy timeCalculationStrategy) {
        if (instance == null) {
            instance = new RideService(fareCalculationStrategy, driverMatchingStrategy, timeCalculationStrategy);
        }
        return instance;
    }

    public synchronized void setFareCalculationStrategy(FareCalculationStrategy fareCalculationStrategy) {
        this.fareCalculationStrategy = fareCalculationStrategy;
    }

    public synchronized void setDriverMatchingStrategy(DriverMatchingStrategy driverMatchingStrategy) {
        this.driverMatchingStrategy = driverMatchingStrategy;
    }

    public synchronized void setTimeCalculationStrategy(TimeCalculationStrategy timeCalculationStrategy) {
        this.timeCalculationStrategy = timeCalculationStrategy;
    }

    public void registerPassenger(Passenger passenger) {
        passengerMap.put(passenger.getId(), passenger);
    }

    public void registerDriver(Driver driver) {
        driverMap.put(driver.getId(), driver);
    }

    public Ride requestRide(Passenger passenger, Location pickupLocation, Location dropOffLocation, RideType rideType, double trafficFactor) {

        // drivers matching
        List<Driver> availableDrivers = driverMatchingStrategy.findDrivers(List.copyOf(driverMap.values()),  pickupLocation, rideType);
        if (availableDrivers.isEmpty()) {
            System.out.println("No drivers available");
            return null;
        }

        System.out.println("Found " + availableDrivers.size() + " drivers");

        // create a ride
        Ride ride = new Ride(rideType, passenger, pickupLocation, dropOffLocation, fareCalculationStrategy);

        // calculate initial fare
        double distance = pickupLocation.calculateDistance(dropOffLocation);
        long durationInMinutes = timeCalculationStrategy.calculateEstimatedRideTime(distance, trafficFactor);

        double estimatedFare = fareCalculationStrategy.calculateFare(ride, distance, durationInMinutes);

        System.out.println("Estimated fare: " + estimatedFare);
        ride.setFare(estimatedFare);

        rideMap.put(ride.getRideId(), ride);

        return ride;
    }

    public void acceptRide(String driverId, String rideId) {
        Driver driver = driverMap.get(driverId);
        Ride ride = rideMap.get(rideId);
        if (driver == null ||  ride == null) {
            System.out.println("Driver or Ride not found");
            return;
        }

        ride.accept(driver);
    }

    public void startRide(String driverId, String rideId) {
        Driver driver = driverMap.get(driverId);
        Ride ride = rideMap.get(rideId);
        if (driver == null ||  ride == null) {
            System.out.println("Driver or Ride not found");
            return;
        }

        ride.start(driver);
    }

    public void completeRide(String driverId, String rideId) {
        Driver driver = driverMap.get(driverId);
        Ride ride = rideMap.get(rideId);
        if (driver == null ||  ride == null) {
            System.out.println("Driver or Ride not found");
            return;
        }

        ride.complete(driver);
    }

    public void cancelRideByPassenger(String rideId) {
        Ride ride = rideMap.get(rideId);
        if (ride == null) {
            System.out.println("Ride not found");
            return;
        }

        ride.cancelByPassenger();
    }

    public void cancelRideByDriver(String driverId, String rideId) {
        Driver driver = driverMap.get(driverId);
        Ride ride = rideMap.get(rideId);
        if (driver == null ||  ride == null) {
            System.out.println("Driver or Ride not found");
            return;
        }

        ride.cancelByDriver(driver);
    }
}
