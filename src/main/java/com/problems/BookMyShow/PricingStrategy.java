package com.problems.BookMyShow;

import java.util.List;

public interface PricingStrategy {
    double calculatePrice(List<Seat> seatList);
}
