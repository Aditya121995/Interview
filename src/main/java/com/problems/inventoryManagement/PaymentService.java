package com.problems.inventoryManagement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PaymentService {
    private final Map<String, Payment> payments;
    private final List<PaymentObserver> paymentObserverList;
    private final PaymentFactory paymentFactory;

    public PaymentService() {
        this.paymentFactory = new PaymentFactory();
        this.payments = new ConcurrentHashMap<>();
        this.paymentObserverList = new CopyOnWriteArrayList<>();
    }

    public void addPaymentObserver(PaymentObserver observer) {
        this.paymentObserverList.add(observer);
    }

    public Payment initiatePayment(String userId, String orderId, double amount, PaymentMethod paymentMethod) {
        Payment payment = new Payment(userId, orderId, amount, paymentMethod);
        this.payments.put(payment.getPaymentId(), payment);
        return payment;
    }

    public void processPayment(String paymentId) {
        if (!payments.containsKey(paymentId)) {
            System.out.println("Payment not found");
            return;
        }

        Payment payment = payments.get(paymentId);
        PaymentProcessor paymentProcessor = paymentFactory.getPaymentProcessor(payment.getPaymentMethod());
        PaymentStatus status = paymentProcessor.process();
        payment.setStatus(status);

        for (PaymentObserver observer : paymentObserverList) {
            System.out.println("Processing Payment " + paymentId);
            observer.onPaymentUpdate(payment);
        }
    }
}
