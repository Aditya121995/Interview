package com.problems.carRentalSystem;

public class PaymentFactory {
    private final PaymentProcessor cashPaymentProcessor;
    private final PaymentProcessor cardPaymentProcessor;
    private final PaymentProcessor upiPaymentProcessor;

    public PaymentFactory() {
        cardPaymentProcessor = new CardPaymentProcessor();
        cashPaymentProcessor = new CashPaymentProcessor();
        upiPaymentProcessor = new UpiPaymentProcessor();
    }

    public PaymentProcessor getPaymentProcessor(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CARD:
                return cardPaymentProcessor;
            case UPI:
                return upiPaymentProcessor;
            case CASH:
                return cashPaymentProcessor;
            default:
                return null;
        }
    }
}
