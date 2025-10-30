package com.problems.BookMyShow;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<Seat> seats;
    @Setter
    private Payment payment;
    @Setter
    private BookingStatus status;
    private final double totalAmount;
    private final LocalDateTime expiredAt;

    public Booking(User user, Show show, List<Seat> seats, double totalAmount) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.PENDING;
        this.totalAmount = totalAmount;
        this.payment = null;
        this.expiredAt = LocalDateTime.now().plusSeconds(15);
    }

    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public void expire() {
        this.status = BookingStatus.EXPIRED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt) && this.status == BookingStatus.PENDING;
    }
}
