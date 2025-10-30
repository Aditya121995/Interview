package com.problems.ParkingLotSystem;

import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLot {
    private final List<ParkingFloor>  floors;
    private final List<EntryGate> entryGates;
    private final List<ExitGate> exitGates;
    private final Map<String, ParkingTicket> activeTickets;
    private final ReentrantLock parkingLock;
    @Setter
    private PricingStrategy pricingStrategy;
    private final PaymentFactory paymentFactory;
    private final ParkingSpotAssignmentStrategy assignmentStrategy;

    public ParkingLot(PricingStrategy pricingStrategy, ParkingSpotAssignmentStrategy assignmentStrategy) {
        this.floors = new ArrayList<>();
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.parkingLock = new ReentrantLock();
        this.pricingStrategy = pricingStrategy;
        this.paymentFactory = new PaymentFactory();
        this.assignmentStrategy = assignmentStrategy;
    }

    public void addFloor(ParkingFloor floor){
        floors.add(floor);
    }

    public void addEntryGate(EntryGate entryGate){
        entryGates.add(entryGate);
        assignmentStrategy.addEntryGate(entryGate, floors);
    }

    public void addExitGate(ExitGate exitGate){
        exitGates.add(exitGate);
    }

    public ParkingTicket parkVehicle(Vehicle vehicle, String entryGateId){
        parkingLock.lock();
        try {
            ParkingSpot parkingSpot = assignmentStrategy.findParkingSpot(floors, vehicle.getVehicleType(), entryGateId);

            if (parkingSpot != null && parkingSpot.park(vehicle)) {
                ParkingTicket parkingTicket = new ParkingTicket(
                        vehicle.getVehicleNumber(),
                        parkingSpot.getSpotId(),
                        getFloorNumber(parkingSpot),
                        LocalDateTime.now()
                );
                activeTickets.put(parkingTicket.getTicketId(), parkingTicket);
                return parkingTicket;
            }


            System.out.println("No space available for :: " + vehicle.getVehicleNumber());
            return null;
        } finally {
            parkingLock.unlock();
        }
    }

    public boolean unparkVehicle(String ticketId, LocalDateTime exitTime, PaymentMode paymentMode){
        parkingLock.lock();
        try {
            ParkingTicket parkingTicket = activeTickets.get(ticketId);
            if (parkingTicket == null) {
                return false;
            }

            ParkingFloor floor = floors.stream()
                    .filter(f -> f.getFloorNumber() == parkingTicket.getFloorNumber())
                    .findFirst().orElse(null);
            if (floor == null) {
                return false;
            }
            ParkingSpot spot = floor.getParkingSpot(parkingTicket.getSpotId());

            // calculate charges
            parkingTicket.setExitTime(exitTime);
            double price = pricingStrategy.calculatePrice(parkingTicket, spot.getSpotType());

            // make payment
            PaymentStrategy paymentStrategy = paymentFactory.getPaymentStrategy(paymentMode);
            parkingTicket.setPrice(price);
            boolean isPaymentDone = paymentStrategy.processPayment(price);

            if (assignmentStrategy.releaseParkingSpot(spot)) {
                // mark exit time on ticket and update the status of ticket
                if (isPaymentDone) {
                    parkingTicket.setTicketActive(false);
                } else {
                    System.out.println("Payment is not done yet.");
                    return false;
                }
                return true;
            }

            return false;
        } finally {
            parkingLock.unlock();
        }
    }

    public Map<Integer, Map<VehicleType, List<String>>> getAvailableParkingSpots(){
        Map<Integer, Map<VehicleType, List<String>>> allAvailableParkingSpots = new HashMap<>(floors.size());
        for (ParkingFloor floor : floors) {
            Map<VehicleType, List<String>> floorAvailableParkingSpots = new HashMap<>();
            for (VehicleType vehicleType : VehicleType.values()) {
                floorAvailableParkingSpots.put(vehicleType, floor.getAvailableParkingSpots(vehicleType));
            }

            allAvailableParkingSpots.put(floor.getFloorNumber(), floorAvailableParkingSpots);
        }

        return allAvailableParkingSpots;
    }

    private int getFloorNumber(ParkingSpot parkingSpot) {
        for (ParkingFloor floor : floors) {
            if (floor.getParkingSpot(parkingSpot.getSpotId()) != null) {
                return floor.getFloorNumber();
            }
        }
        return -1;
    }


}
