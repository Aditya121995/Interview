package com.problems.bookMyShow;

public class PaymentFactory {
    private final PaymentProcessor upiProcessor;
    private final PaymentProcessor debitCardProcessor;

    public PaymentFactory() {
        upiProcessor=new UPIProcessor();
        debitCardProcessor=new DebitCardProcessor();
    }

    public PaymentProcessor getPaymentProcessor(PaymentMethod paymentMethod){
        switch (paymentMethod){
            case UPI:
                return upiProcessor;
            case DEBIT_CARD:
                return debitCardProcessor;
            default:
                System.out.println("Invalid PaymentMethod");
                return null;
        }
    }
}
