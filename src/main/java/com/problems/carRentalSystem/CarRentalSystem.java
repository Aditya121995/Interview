package com.problems.carRentalSystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarRentalSystem {
    private final VehicleInventoryManager vehicleInventoryManager;
    private final ReservationManager reservationManager;
    private final Map<String, Customer> customers;

    public CarRentalSystem() {
        this.customers = new ConcurrentHashMap<>();
        this.vehicleInventoryManager = new VehicleInventoryManager();
        this.reservationManager = new ReservationManager(vehicleInventoryManager);
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleInventoryManager.addVehicle(vehicle);
    }

    public List<Vehicle> searchVehicles(SearchCriteria criteria) {
        List<Vehicle> vehicles = vehicleInventoryManager.searchVehicle(criteria);
        System.out.println("Searched vehicles :: " + vehicles);
        return vehicles;
    }

    public Reservation bookVehicle(String customerId, String vehicleId,
                                   LocalDateTime pickupDate,
                                   LocalDateTime returnDate) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return null;
        }

        Reservation reservation = reservationManager.createReservation(customerId, vehicleId, pickupDate, returnDate);
        System.out.println("Reservation :: " + reservation);

        return reservation;
    }

    public void cancelBooking(String customerId, String reservationId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }

        reservationManager.cancelReservation(customerId, reservationId);
    }

    public void payForBooking(String customerId, String reservationId, LocalDateTime dropDate, PaymentMethod paymentMethod) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
        }

        reservationManager.payForReservation(customerId, reservationId, dropDate,  paymentMethod);
    }
}
