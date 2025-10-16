package com.problems.carRentalSystem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VehicleInventoryManager {
    Map<String, Vehicle> vehicles;

    public VehicleInventoryManager() {
        vehicles=new ConcurrentHashMap<>();
    }

    public synchronized void addVehicle(Vehicle vehicle){
        vehicles.put(vehicle.getVehicleId(), vehicle);
    }

    public synchronized void removeVehicle(Vehicle vehicle){
        vehicles.remove(vehicle.getVehicleId());
    }

    public synchronized List<Vehicle> searchVehicle(SearchCriteria searchCriteria){
        return vehicles.values().stream()
                .filter(v -> matchesCriteria(v, searchCriteria))
                .filter(v -> v.getStatus().equals(VehicleStatus.AVAILABLE))
                .collect(Collectors.toList());
    }

    public synchronized Vehicle getVehicleById(String id){
        return vehicles.get(id);
    }

    private boolean matchesCriteria(Vehicle v, SearchCriteria searchCriteria) {
        boolean match = true;
        if (searchCriteria.getVehicleType() != null && !searchCriteria.getVehicleType().equals(v.getVehicleType())) {
            match = false;
        }

        if (searchCriteria.getMinPricePerHour() != null && searchCriteria.getMinPricePerHour() > v.getPricePerHour()) {
            match = false;
        }

        if (searchCriteria.getMaxPricePerHour() != null && searchCriteria.getMaxPricePerHour() < v.getPricePerHour()) {
            match = false;
        }

        return match;
    }
}
