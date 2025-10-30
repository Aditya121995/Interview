package com.problems.BookMyShow;

import java.util.List;

public class WeekdayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(List<Seat> seatList) {
        double totalPrice=0;
        for(Seat seat:seatList){
            totalPrice+=seat.getPrice();
        }

        return totalPrice;
    }
}
