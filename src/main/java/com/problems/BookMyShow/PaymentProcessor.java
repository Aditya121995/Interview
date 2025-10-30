package com.problems.BookMyShow;


public interface PaymentProcessor {
    PaymentStatus process(Payment payment);
}
