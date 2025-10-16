package com.problems.carRentalSystem;

public class CardPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(Reservation reservation) {
        System.out.println("Card Payment Received for reservation: " + reservation.getReservationId() +
                " of amount :: " + reservation.getTotalPrice());
        return true;
    }
}
