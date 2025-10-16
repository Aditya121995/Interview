package com.problems.carRentalSystem;

public class UpiPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(Reservation reservation) {
        System.out.println("Upi Payment received for reservation: " + reservation.getReservationId() +
                " of amount :: " + reservation.getTotalPrice());
        return true;
    }
}
