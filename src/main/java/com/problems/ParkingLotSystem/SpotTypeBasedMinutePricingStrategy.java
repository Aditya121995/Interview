package com.problems.ParkingLotSystem;

import java.util.HashMap;
import java.util.Map;

public class SpotTypeBasedMinutePricingStrategy implements PricingStrategy {
    private final Map<SpotType, Double> basePrices;

    public SpotTypeBasedMinutePricingStrategy() {
        this.basePrices = new HashMap<>();
    }

    @Override
    public void addBasePrice(SpotType spotType, double price) {
        basePrices.put(spotType, price);
    }

    @Override
    public double calculatePrice(ParkingTicket ticket, SpotType spotType) {
        long hours = ticket.getExitTime().getHour() - ticket.getEntryTime().getHour();
        long minutes = hours*60 + ticket.getExitTime().getMinute() - ticket.getEntryTime().getMinute();
        double price = basePrices.get(spotType);
        return price * minutes;
    }
}
