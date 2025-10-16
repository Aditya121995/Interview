package com.problems.carRentalSystem;

public class CashPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(Reservation reservation) {
        System.out.println("Cash Payment Received for reservation: " + reservation.getReservationId() +
                " of amount :: " + reservation.getTotalPrice());
        return true;
    }
}
