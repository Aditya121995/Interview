package com.problems.parkingLotSystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class NearestToEntryParkingSpotAssignmentStrategy implements ParkingSpotAssignmentStrategy {
    private final Map<String, Map<VehicleType, PriorityBlockingQueue<ParkingSpot>>> spotByGateAndType;


    public NearestToEntryParkingSpotAssignmentStrategy() {
        this.spotByGateAndType=new ConcurrentHashMap<>();
    }

    public void addEntryGate(EntryGate entryGate, List<ParkingFloor> parkingFloors){
        String gateId = entryGate.getGateId();
        Coordinate gateCoordinate = entryGate.getCoordinate();

        Map<VehicleType, PriorityBlockingQueue<ParkingSpot>> typeMap = new ConcurrentHashMap<>();

        for (VehicleType vehicleType : VehicleType.values()) {
            PriorityBlockingQueue<ParkingSpot> heap = new PriorityBlockingQueue<>(100,
                    Comparator.comparingDouble(spot -> spot.getCoordinate().distanceFrom(gateCoordinate)));
            for (ParkingFloor parkingFloor : parkingFloors) {
                for (ParkingSpot spot : parkingFloor.getAllParkingSpots()) {
                    if (spot.canMakeParking(vehicleType)) {
                        heap.offer(spot);
                    }
                }
            }

            typeMap.put(vehicleType, heap);
        }

        spotByGateAndType.put(gateId, typeMap);
    }


    @Override
    public ParkingSpot findParkingSpot(List<ParkingFloor> floors, VehicleType type, String entryGateId) {
        Map<VehicleType, PriorityBlockingQueue<ParkingSpot>> typeMap = spotByGateAndType.get(entryGateId);
        if (typeMap == null) {
            return null;
        }

        PriorityBlockingQueue<ParkingSpot> heap = typeMap.get(type);
        if (heap == null || heap.isEmpty()) {
            return null;
        }

        ParkingSpot nearestParkingSpot = null;
        List<ParkingSpot> tempRemovedSpots = new ArrayList<>();

        while (!heap.isEmpty()) {
            ParkingSpot spot = heap.poll();
            if (spot.isAvailable() && spot.canMakeParking(type)) {
                nearestParkingSpot = spot;
                break;
            }

            tempRemovedSpots.add(spot);
        }

        heap.addAll(tempRemovedSpots);

        return nearestParkingSpot;
    }

    @Override
    public boolean releaseParkingSpot(ParkingSpot spot) {
        for (Map.Entry<String, Map<VehicleType, PriorityBlockingQueue<ParkingSpot>>> entry : spotByGateAndType.entrySet()) {
            Map<VehicleType, PriorityBlockingQueue<ParkingSpot>> typeMap = entry.getValue();

            for (VehicleType vehicleType : VehicleType.values()) {
                if(!spot.canMakeParking(vehicleType)) {
                    continue;
                }
                PriorityBlockingQueue<ParkingSpot> heap = typeMap.get(vehicleType);
                if (heap != null) {
                    if (!heap.contains(spot)) {
                        heap.offer(spot);
                    }
                }
            }
        }

        return spot.unpark();
    }
}
