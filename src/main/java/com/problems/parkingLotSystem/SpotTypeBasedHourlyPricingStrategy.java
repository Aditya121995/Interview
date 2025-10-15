package com.problems.parkingLotSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SpotTypeBasedPricingStrategy implements PricingStrategy {
    private final Map<SpotType, Double> basePrices;

    public SpotTypeBasedPricingStrategy() {
        this.basePrices = new HashMap<>();
    }

    public void addBasePrice(SpotType spotType, double price) {
        basePrices.put(spotType, price);
    }
}
