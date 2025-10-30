package com.problems.ParkingLotSystem;

public class PaymentFactory {
    private final PaymentStrategy paymentStrategyUpi;
    private final PaymentStrategy paymentStrategyCash;
    private final PaymentStrategy paymentStrategyDebitCard;
    private final PaymentStrategy paymentStrategyCreditCard;

    public PaymentFactory() {
        this.paymentStrategyUpi = new PaymentStrategyUPI();
        this.paymentStrategyCash = new PaymentStrategyCash();
        this.paymentStrategyDebitCard = new PaymentStrategyDebitCard();
        this.paymentStrategyCreditCard = new PaymentStrategyCreditCard();
    }

    public PaymentStrategy getPaymentStrategy(PaymentMode paymentMode) {
        switch (paymentMode) {
            case UPI:
                return paymentStrategyUpi;
            case CASH:
                return paymentStrategyCash;
            case DEBIT_CARD:
                return paymentStrategyDebitCard;
            case CREDIT_CARD:
                return paymentStrategyCreditCard;
            default:
                return null;
        }
    }
}
