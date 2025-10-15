package com.problems.parkingLotSystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingFloor {
    private final int floorNumber;
    private final Map<SpotType, List<ParkingSpot>> spotsByType;
    private final Map<String, ParkingSpot> spotsMap;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spotsMap=new ConcurrentHashMap<>();
        this.spotsByType=new ConcurrentHashMap<>();

        for(SpotType s: SpotType.values()){
            spotsByType.put(s, new ArrayList<>());
        }
    }

    public void addParkingSpot(ParkingSpot spot){
        spotsMap.put(spot.getSpotId(), spot);
        spotsByType.get(spot.getSpotType()).add(spot);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ParkingSpot getParkingSpot(String spotId){
        return spotsMap.get(spotId);
    }

    public List<ParkingSpot> getAllParkingSpots(){
        return new ArrayList<>(spotsMap.values());
    }

    public ParkingSpot findAvailableParkingSpots(VehicleType vehicleType){
        List<SpotType> spotPreference = getSpotPreference(vehicleType);

        if (spotPreference == null) return null;

        for(SpotType s: spotPreference){
            List<ParkingSpot> spotList = spotsByType.get(s);
            if(spotList.isEmpty()){
                continue;
            }

            Optional<ParkingSpot> parkingSpotOptional = spotList.stream()
                    .filter(sp -> sp.isAvailable())
                    .filter(parkingSpot -> parkingSpot.canMakeParking(vehicleType))
                    .findFirst();
            if(parkingSpotOptional.isPresent()){
                return parkingSpotOptional.get();
            }
        }

        return null;
    }

    public List<String> getAvailableParkingSpots(VehicleType vehicleType){
        List<SpotType> spotPreference = getSpotPreference(vehicleType);
        if (spotPreference == null) return null;
        List<String> parkingSpotList = new ArrayList<>();

        for(SpotType s: spotPreference){
            List<ParkingSpot> spotList = spotsByType.get(s);
            if(spotList.isEmpty()){
                continue;
            }
            parkingSpotList.addAll(spotList.stream()
                    .filter(sp -> sp.isAvailable())
                    .filter(parkingSpot -> parkingSpot.canMakeParking(vehicleType))
                    .map(ParkingSpot::getSpotId)
                    .toList());
        }

        return parkingSpotList;
    }

    private List<SpotType> getSpotPreference(VehicleType vehicleType){
        switch (vehicleType){
            case CAR -> {
                return Arrays.asList(SpotType.MEDIUM, SpotType.LARGE);
            }
            case BIKE ->  {
                return Arrays.asList(SpotType.SMALL, SpotType.MEDIUM, SpotType.LARGE);
            }
            case TRUCK -> {
                return Arrays.asList(SpotType.LARGE);
            }
        }

        return null;
    }


}
