package com.problems.ParkingLotSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotTypeBasedHybridPricingStrategy implements PricingStrategy {

    private final Map<SpotType, List<Double>> basePrices;
    private final double defaultBasePrice;

    public SpotTypeBasedHybridPricingStrategy(double defaultBasePrice) {
        this.basePrices = new HashMap<>();
        this.defaultBasePrice = defaultBasePrice;
    }

    @Override
    public void addBasePrice(SpotType spotType, double price) {
        basePrices.computeIfAbsent(spotType, k->new ArrayList<>()).add(price);
    }

    @Override
    public double calculatePrice(ParkingTicket ticket, SpotType spotType) {
        long hours = ticket.getExitTime().getHour() - ticket.getEntryTime().getHour() + 1;
        List<Double> rates = basePrices.get(spotType);

        if (rates == null || rates.isEmpty()) {
            return defaultBasePrice * hours;
        }

        double price =  rates.get(rates.size()-1);
        for(int i=1; i<rates.size(); i++){
            if(i>=hours){
                price = rates.get(i-1);
                break;
            }
        }
        return price * hours;
    }
}
