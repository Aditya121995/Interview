package com.problems.ParkingLotSystem;

public interface PricingStrategy {
    double calculatePrice(ParkingTicket ticket, SpotType spotType);
    void addBasePrice(SpotType spotType, double price);
}
