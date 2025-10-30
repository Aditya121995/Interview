package com.problems.ParkingLotSystem;

import java.time.LocalDateTime;

public class ParkingLotDemo {

    public static void main(String[] args) {
        PricingStrategy pricingStrategy =  new SpotTypeBasedMinutePricingStrategy();
        pricingStrategy.addBasePrice(SpotType.SMALL, 1.0);
        pricingStrategy.addBasePrice(SpotType.MEDIUM, 2.0);
        pricingStrategy.addBasePrice(SpotType.LARGE, 3.0);
        ParkingSpotAssignmentStrategy parkingSpotAssignmentStrategy = new FirstAvailableParkingSpotAssignmentStrategy();
        ParkingLot parkingLot = new ParkingLot(pricingStrategy, parkingSpotAssignmentStrategy);

        // Intialize floors
        ParkingFloor floor1 = new ParkingFloor(1);
        ParkingFloor floor2 = new ParkingFloor(2);

        floor1.addParkingSpot(new ParkingSpot("A1", SpotType.SMALL, new Coordinate(1, 1)));
        floor1.addParkingSpot(new ParkingSpot("A2", SpotType.SMALL, new Coordinate(1, 2)));
        floor1.addParkingSpot(new ParkingSpot("A3", SpotType.MEDIUM, new Coordinate(1, 3)));
        floor1.addParkingSpot(new ParkingSpot("A4", SpotType.MEDIUM, new Coordinate(2, 1)));
        floor1.addParkingSpot(new ParkingSpot("A5", SpotType.MEDIUM, new Coordinate(2, 2)));
        floor1.addParkingSpot(new ParkingSpot("A6", SpotType.LARGE, new Coordinate(2, 3)));
        floor1.addParkingSpot(new ParkingSpot("A7", SpotType.LARGE, new Coordinate(3, 3)));

        floor2.addParkingSpot(new ParkingSpot("A1", SpotType.SMALL, new Coordinate(1, 1)));
        floor2.addParkingSpot(new ParkingSpot("A2", SpotType.SMALL, new Coordinate(2, 1)));
        floor2.addParkingSpot(new ParkingSpot("A3", SpotType.SMALL, new Coordinate(1, 2)));
        floor2.addParkingSpot(new ParkingSpot("A4", SpotType.SMALL, new Coordinate(2, 2)));
        floor2.addParkingSpot(new ParkingSpot("A5", SpotType.MEDIUM, new Coordinate(3, 1)));
        floor2.addParkingSpot(new ParkingSpot("A6", SpotType.MEDIUM, new Coordinate(3, 2)));
        floor2.addParkingSpot(new ParkingSpot("A7", SpotType.MEDIUM, new Coordinate(3, 3)));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);
        parkingLot.addEntryGate(new EntryGate("E1", 1, new Coordinate(0, 0)));
        parkingLot.addEntryGate(new EntryGate("E2", 2, new Coordinate(4, 4)));
        parkingLot.addExitGate(new ExitGate("E1", 1, new Coordinate(0, 4)));
        parkingLot.addExitGate(new ExitGate("E2", 2, new Coordinate(4, 0)));

        Vehicle vehicle1 = new Car("KA-5679");
        Vehicle vehicle2 = new Bike("KA-5235");
        Vehicle vehicle21 = new Bike("KA-5234");
        Vehicle vehicle22 = new Bike("KA-5235");
        Vehicle vehicle3 = new Truck("KA-8239");
        Vehicle vehicle4 = new Truck("KA-8235");
        Vehicle vehicle5 = new Truck("KA-8528");

        System.out.println("=======Parking=======");

        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t1 = parkingLot.parkVehicle(vehicle1, "E1");
        System.out.println("Ticket :: " + t1.toString());
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t2 = parkingLot.parkVehicle(vehicle2, "E1");
        System.out.println("Ticket :: " + t2);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t3 = parkingLot.parkVehicle(vehicle3, "E1");
        System.out.println("Ticket :: " + t3);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t4 = parkingLot.parkVehicle(vehicle4, "E1");
        System.out.println("Ticket :: " + t4);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t5 = parkingLot.parkVehicle(vehicle5, "E1");
        System.out.println("Ticket :: " + t5);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());

        ParkingTicket t6 = parkingLot.parkVehicle(vehicle21, "E1");
        System.out.println("Ticket :: " + t6);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        ParkingTicket t7 = parkingLot.parkVehicle(vehicle22, "E1");
        System.out.println("Ticket :: " + t7);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());


        System.out.println("=======UnParking=======");

        parkingLot.unparkVehicle(t1.getTicketId(), LocalDateTime.now().plusHours(3), PaymentMode.CASH);
        System.out.println("Ticket :: " + t1);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        parkingLot.unparkVehicle(t2.getTicketId(), LocalDateTime.now().plusHours(5), PaymentMode.UPI);
        System.out.println("Ticket :: " + t2);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        parkingLot.unparkVehicle(t3.getTicketId(), LocalDateTime.now().plusHours(2),  PaymentMode.DEBIT_CARD);
        System.out.println("Ticket :: " + t3);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
        parkingLot.unparkVehicle(t4.getTicketId(), LocalDateTime.now().plusHours(1), PaymentMode.CREDIT_CARD);
        System.out.println("Ticket :: " + t4);
        System.out.println("Available spots :: " + parkingLot.getAvailableParkingSpots());
    }
}
