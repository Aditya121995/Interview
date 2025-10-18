package com.problems.bookMyShow;


import com.problems.carRentalSystem.PaymentMethod;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<Seat> seats;
    private Payment payment;
    private BookingStatus status;

    public Booking(User user, Show show, List<Seat> seats, double totalAmount) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.PENDING;
        this.payment = new Payment(BigDecimal.valueOf(totalAmount));
    }

    public void confirm(PaymentMethod paymentMethod) {
        payment.processPayment(paymentMethod);
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            this.status = BookingStatus.CONFIRMED;
        } else if (payment.getStatus() == PaymentStatus.FAILED) {
            this.status = BookingStatus.CANCELLED;
        }
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public void expire() {
        this.status = BookingStatus.EXPIRED;
    }
}
