package com.problems.carRentalSystem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@ToString
public class Reservation {
    private final String reservationId;
    private final String customerId;
    private final String vehicleId;
    @Setter
    private LocalDateTime pickupDate;
    @Setter
    private LocalDateTime dropoffDate;
    @Setter
    private double totalPrice;
    @Setter
    private ReservationStatus reservationStatus;
    @Setter
    private LocalDateTime actualDropoffDate;
    // add additional services

    private static final double LATE_PENALTY = 100.0;

    public Reservation(String customerId, String vehicleId, LocalDateTime pickupDate, LocalDateTime dropoffDate, double vehiclePriceHourly) {
        this.reservationId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.pickupDate = pickupDate;
        this.dropoffDate = dropoffDate;
        this.totalPrice = calculateTotalPrice(vehiclePriceHourly);
        this.reservationStatus = ReservationStatus.CONFIRMED;
    }

    public double calculateTotalPrice(double vehiclePriceHourly) {
        long hours = ChronoUnit.HOURS.between(pickupDate, dropoffDate);
        return (vehiclePriceHourly * hours);
    }

    public void addLateFee() {
        long hours = ChronoUnit.HOURS.between(dropoffDate, actualDropoffDate);
        totalPrice += LATE_PENALTY * hours;
    }
}
