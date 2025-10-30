package com.problems.ParkingLotSystem;

import java.util.List;

public class FirstAvailableParkingSpotAssignmentStrategy implements ParkingSpotAssignmentStrategy {
    @Override
    public ParkingSpot findParkingSpot(List<ParkingFloor> floors, VehicleType type, String entryGateId) {
        for (ParkingFloor floor : floors) {
            ParkingSpot parkingSpot = floor.findAvailableParkingSpots(type);
            if (parkingSpot != null) return parkingSpot;
        }

        return null;
    }

    @Override
    public boolean releaseParkingSpot(ParkingSpot spot) {
        return spot.unpark();
    }

    @Override
    public void addEntryGate(EntryGate entryGate, List<ParkingFloor> parkingFloors) {
        return;
    }
}
