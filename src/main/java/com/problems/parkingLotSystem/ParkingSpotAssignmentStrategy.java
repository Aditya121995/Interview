package com.problems.parkingLotSystem;

import java.util.List;

public interface ParkingSpotAssignmentStrategy {
    ParkingSpot findParkingSpot(List<ParkingFloor> floors, VehicleType type, String entryGateId);
    boolean releaseParkingSpot(ParkingSpot spot);
    void addEntryGate(EntryGate entryGate, List<ParkingFloor> parkingFloors);
}
