package com.problems.carRentalSystem;

import lombok.Getter;

@Getter
public class Customer {
    private final String customerId;
    private final String customerName;
    private final String drivingLicense;

    public Customer(String customerId, String customerName, String drivingLicense) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.drivingLicense = drivingLicense;
    }
}
