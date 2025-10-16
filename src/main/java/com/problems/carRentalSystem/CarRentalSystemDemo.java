package com.problems.carRentalSystem;

import java.time.LocalDateTime;

public class CarRentalSystemDemo {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem=new CarRentalSystem();

        rentalSystem.registerCustomer(new Customer("c1", "Aditya", "1234"));
        rentalSystem.registerCustomer(new Customer("c2", "Aditya", "1234"));
        rentalSystem.registerCustomer(new Customer("c3", "Aditya", "1234"));
        rentalSystem.registerCustomer(new Customer("c4", "Aditya", "1234"));

        rentalSystem.addVehicle(new Car("v1", "Swift", "4543", 100.0));
        rentalSystem.addVehicle(new Car("v2", "Baleno", "3225", 200.0));
        rentalSystem.addVehicle(new Car("v3", "VagonR", "3225", 50.0));
        rentalSystem.addVehicle(new SUV("v4", "Inova", "3225", 300.0));
        rentalSystem.addVehicle(new SUV("v5", "Ertiga", "3225", 250.0));

        SearchCriteria.SearchCriteriaBuilder criteria =  SearchCriteria.builder();
        rentalSystem.searchVehicles(criteria.build());

        criteria.vehicleType(VehicleType.CAR);
        rentalSystem.searchVehicles(criteria.build());

        criteria.maxPricePerHour(150.0);
        rentalSystem.searchVehicles(criteria.build());

        Reservation reservation = rentalSystem.bookVehicle("c1", "v1", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        rentalSystem.payForBooking("c1", reservation.getReservationId(),
                LocalDateTime.now().plusDays(3), PaymentMethod.CARD);
    }
}
