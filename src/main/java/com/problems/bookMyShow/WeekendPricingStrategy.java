package com.problems.bookMyShow;

import java.util.List;

public class WeekendPricingStrategy implements PricingStrategy {
    private static final double WEEKEND_MULTIPLIER = 1.2;
    @Override
    public double calculatePrice(List<Seat> seatList) {
        double totalPrice=0;
        for(Seat seat:seatList){
            totalPrice+=seat.getPrice()*WEEKEND_MULTIPLIER;
        }

        return totalPrice;
    }
}
