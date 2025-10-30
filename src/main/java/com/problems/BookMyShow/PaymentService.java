package com.problems.BookMyShow;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PaymentService {
    private final Map<String, Payment> payments;
    private final List<PaymentObserver> paymentObservers;
    private final PaymentFactory paymentFactory;


    public PaymentService() {
        this.payments = new ConcurrentHashMap<>();
        this.paymentObservers = new CopyOnWriteArrayList<>();
        this.paymentFactory = new PaymentFactory();
    }

    public void addPaymentObserver(PaymentObserver observer) {
        this.paymentObservers.add(observer);
    }

    public Payment initiatePayment(String userId, String orderId, double amount, PaymentMethod paymentMethod) {
        Payment payment = new Payment(userId, orderId, amount, paymentMethod);
        this.payments.put(payment.getPaymentId(), payment);
        return payment;
    }

    public void processPayment(String paymentId) {
        if (!this.payments.containsKey(paymentId)) {
            System.out.println("Payment not found");
            return;
        }

        Payment payment = this.payments.get(paymentId);
        PaymentProcessor paymentProcessor = paymentFactory.getPaymentProcessor(payment.getPaymentMethod());
        PaymentStatus paymentStatus = paymentProcessor.process(payment);
        payment.setStatus(paymentStatus);

        for (PaymentObserver observer : paymentObservers) {
            observer.onPaymentUpdate(payment);
        }

        System.out.println("Payment processed :: " + payment);

    }
}
