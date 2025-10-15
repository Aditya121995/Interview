package com.problems.parkingLotSystem;

import java.util.HashMap;
import java.util.Map;

public class SpotTypeBasedHourlyPricingStrategy implements PricingStrategy {
    private final Map<SpotType, Double> basePrices;

    public SpotTypeBasedHourlyPricingStrategy() {
        this.basePrices = new HashMap<>();
    }

    @Override
    public void addBasePrice(SpotType spotType, double price) {
        basePrices.put(spotType, price);
    }

    @Override
    public double calculatePrice(ParkingTicket ticket, SpotType spotType) {
        long hours = ticket.getExitTime().getHour() - ticket.getEntryTime().getHour();
        double price = basePrices.get(spotType);
        return price * hours;
    }
}
