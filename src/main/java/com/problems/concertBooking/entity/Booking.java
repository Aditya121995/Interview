package com.problems.concertBooking.entity;

import com.problems.concertBooking.enums.BookingStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Booking {
    private final String id;
    private final Concert concert;
    private final User user;
    private final Long amount;
    private final List<Seat> seats;
    private BookingStatus status;

    public Booking(Concert concert, User user, List<Seat> seats) {
        this.id = UUID.randomUUID().toString();
        this.concert = concert;
        this.user = user;
        this.seats =  seats;
        this.amount = calculateAmount(seats);
        this.status = BookingStatus.PENDING;
    }

    private Long calculateAmount(List<Seat> seats) {
        return seats.stream().mapToLong(Seat::getPrice).sum();
    }

    public void cancelBooking(){
        if(BookingStatus.BOOKED.equals(status)){
            this.status = BookingStatus.CANCELLED;
            seats.forEach(Seat::release);
            System.out.println("Booking cancelled :: " + id);
            return;
        }

        throw new IllegalArgumentException("Invalid booking status for cancellation");
    }

    public void confirmBooking(){
        if (BookingStatus.PENDING.equals(status)) {
            this.status = BookingStatus.BOOKED;
            seats.forEach(Seat::book);
            return;
        }

        throw new IllegalArgumentException("Invalid booking status for confirmation");
    }
}
