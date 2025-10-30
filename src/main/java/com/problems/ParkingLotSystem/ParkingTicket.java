package com.problems.ParkingLotSystem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
public class ParkingTicket {
    private final String ticketId;
    private final String vehicleNumber;
    private final String spotId;
    private final int floorNumber;
    private final LocalDateTime entryTime;
    @Setter
    private LocalDateTime exitTime;
    @Setter
    private boolean isTicketActive;
    @Setter
    private double price;

    public ParkingTicket(String vehicleNumber,
                         String spotId,
                         int floorNumber,
                         LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicleNumber = vehicleNumber;
        this.spotId = spotId;
        this.floorNumber = floorNumber;
        this.entryTime = entryTime;
        this.isTicketActive = true;
    }
}
