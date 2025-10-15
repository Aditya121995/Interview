package com.problems.parkingLotSystem;

public interface PricingStrategy {
    double calculatePrice(ParkingTicket ticket, SpotType spotType);
    void addBasePrice(SpotType spotType, double price);
}
