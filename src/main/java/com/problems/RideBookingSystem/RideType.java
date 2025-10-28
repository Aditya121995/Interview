package com.problems.RideBookingSystem;

public enum RideType {
    REGULAR(1.0), PREMIUM(1.5), LUXURY(2);

    private final double priceMultiplier;

    RideType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
