package com.problems.concertBooking.entity;

import com.problems.concertBooking.enums.SeatStatus;
import com.problems.concertBooking.enums.SeatType;
import lombok.Data;

import java.util.UUID;

@Data
public class Seat {
    private final String id;
    private Long price;
    private final String seatNumber;
    private SeatType seatType;
    private SeatStatus status;


    public Seat(Long price, String seatNumber, SeatType seatType, SeatStatus status) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = SeatStatus.AVAILABLE;
    }

    public synchronized void reserve() {
        if (status == SeatStatus.AVAILABLE) {
            this.status = SeatStatus.RESERVED;
            return;
        }
        throw  new RuntimeException("Seat is already booked or reserved");
    }

    public synchronized void book() {
        if (status == SeatStatus.RESERVED) {
            this.status = SeatStatus.BOOKED;
            return;
        }
        throw  new RuntimeException("Seat is already booked or reserved");
    }

    public void release() {
        if (status == SeatStatus.BOOKED) {
            this.status = SeatStatus.AVAILABLE;
        }
    }
}
