package com.problems.carRentalSystem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationManager {
    Map<String, Reservation> reservations;
    private final VehicleInventoryManager vehicleInventoryManager;
    private final PaymentFactory paymentFactory;

    public ReservationManager(VehicleInventoryManager vehicleInventoryManager) {
        this.reservations = new ConcurrentHashMap<>();
        this.vehicleInventoryManager = vehicleInventoryManager;
        paymentFactory=new PaymentFactory();
    }

    public synchronized Reservation createReservation(String customerId, String vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate) {
        if (!isVehicleAvailable(vehicleId, pickupDate, returnDate)) {
            System.out.println("Vehicle Not Available");
            return null;
        }

        Vehicle vehicle = vehicleInventoryManager.getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle Not Present");
            return null;
        }

        Reservation reservation = new Reservation(customerId, vehicleId, pickupDate, returnDate, vehicle.getPricePerHour());

        reservations.put(reservation.getReservationId(), reservation);
        return reservation;
    }

    public synchronized void modifyReservation(String reservationId, LocalDateTime newPickupDate, LocalDateTime newDropoffDate) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("Reservation Not Available");
            return;
        }

        if (reservation.getReservationStatus().equals(ReservationStatus.COMPLETED) ||
                reservation.getReservationStatus().equals(ReservationStatus.CANCELLED)) {
            System.out.println("Cannot modify COMPLETED/CANCELLED reservation");
            return;
        }

        reservation.setPickupDate(newPickupDate);
        reservation.setDropoffDate(newDropoffDate);
    }

    public synchronized void cancelReservation(String customerId, String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("Reservation Not Available");
            return;
        }

        if (!reservation.getCustomerId().equals(customerId)) {
            System.out.println("Cannot cancel reservation. Customer Id does not match");
            return;
        }

        Vehicle vehicle = vehicleInventoryManager.getVehicleById(reservation.getVehicleId());
        if (vehicle == null) {
            System.out.println("Vehicle Not Available");
            return;
        }

        if (reservation.getReservationStatus().equals(ReservationStatus.COMPLETED) ||
                reservation.getReservationStatus().equals(ReservationStatus.CANCELLED)) {
            System.out.println("Cannot cancel COMPLETED/CANCELLED/ACTIVE reservation");
            return;
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        System.out.println("Reservation :: " + reservation);
    }

    public synchronized void payForReservation(String customerId, String reservationId, LocalDateTime dropOffDate, PaymentMethod paymentMethod) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("Reservation Not Available");
            return;
        }

        if (!reservation.getCustomerId().equals(customerId)) {
            System.out.println("Cannot complete reservation. Customer Id does not match");
            return;
        }

        // calculate price
        reservation.setActualDropoffDate(dropOffDate);
        if (dropOffDate.isAfter(reservation.getDropoffDate())) {
            reservation.addLateFee();
        }

        // make payment
        PaymentProcessor paymentProcessor = paymentFactory.getPaymentProcessor(paymentMethod);
        boolean paymentDone = paymentProcessor.process(reservation);

        // update reservation status to completed if payment is successful
        if (paymentDone) {
            reservation.setReservationStatus(ReservationStatus.COMPLETED);
            System.out.println("Reservation paid :: " + reservation);
        } else {
            System.out.println("Reservation payment failed :: " + reservation);
        }
    }

    private boolean isVehicleAvailable(String vehicleId, LocalDateTime pickupDate, LocalDateTime returnDate) {
        Optional<Reservation> reservationOptional =  reservations.values().stream()
                .filter(reservation -> reservation.getVehicleId().equals(vehicleId))
                .filter(reservation -> !reservation.getReservationStatus().equals(ReservationStatus.CANCELLED) &&
                        !reservation.getReservationStatus().equals(ReservationStatus.COMPLETED))
                .filter(reservation -> !reservation.getPickupDate().isAfter(returnDate) && !reservation.getDropoffDate().isBefore(pickupDate))
                .findFirst();

        return reservationOptional.isEmpty();
    }
}
