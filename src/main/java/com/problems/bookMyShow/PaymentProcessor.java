package com.problems.bookMyShow;


public interface PaymentProcessor {
    PaymentStatus process(Payment payment);
}
