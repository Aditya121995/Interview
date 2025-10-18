package com.problems.bookMyShow;

import java.util.List;

public interface PricingStrategy {
    double calculatePrice(List<Seat> seatList);
}
