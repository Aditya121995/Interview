package com.problems.ParkingLotSystem;

import java.util.HashMap;
import java.util.Map;

public class SpotTypeBasedFixedPricingStrategy implements PricingStrategy {
    private final Map<SpotType, Double> basePrices;

    public SpotTypeBasedFixedPricingStrategy() {
        this.basePrices = new HashMap<>();
    }

    @Override
    public void addBasePrice(SpotType spotType, double price) {
        basePrices.put(spotType, price);
    }

    @Override
    public double calculatePrice(ParkingTicket ticket, SpotType spotType) {
        return basePrices.get(spotType);
    }
}
