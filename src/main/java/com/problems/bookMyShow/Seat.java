package com.problems.bookMyShow;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Seat {
    private final String id;
    private final String seatNumber;
    private final SeatType type;
    @Setter
    private SeatStatus status;
    @Setter
    private String lockedByUserId;
    @Setter
    private LocalDateTime lockExpiry;
    @Setter
    private double price;

    public Seat(String id, String seatNumber, SeatType type) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
        this.lockedByUserId = null;
        this.lockExpiry = null;
        this.price = 0;
    }

    public boolean lock(String userId, LocalDateTime lockExpiry){
        if(this.status == SeatStatus.AVAILABLE){
            this.lockExpiry = lockExpiry;
            this.status = SeatStatus.LOCKED;
            this.lockedByUserId = userId;
            return true;
        }

        return false;
    }

    public boolean unlock(){
        System.out.println("Lock released started");
        if(this.status == SeatStatus.LOCKED){
            this.lockExpiry = null;
            this.status = SeatStatus.AVAILABLE;
            this.lockedByUserId = null;
            System.out.println("Lock released for seat : " + this.seatNumber);
            return true;
        }

        return false;
    }

    public boolean book(){
        if(this.status == SeatStatus.LOCKED){
            this.status = SeatStatus.BOOKED;
            this.lockExpiry = null;
            this.lockedByUserId = null;
            return true;
        }

        return false;
    }

    public boolean isLockExpired(){
        return this.lockExpiry != null && this.lockExpiry.isBefore(LocalDateTime.now());
    }
}
