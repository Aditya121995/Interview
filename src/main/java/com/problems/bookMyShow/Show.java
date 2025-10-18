package com.problems.bookMyShow;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Getter
public class Show {
    private final String id;
    private final Movie movie;
    private final Theatre theatre;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final PricingStrategy pricingStrategy;
    private final Map<String, Seat> seatMap;
    private final ReentrantLock lock;

    public Show(String id, Movie movie, Theatre theatre, Screen screen, LocalDateTime startTime, PricingStrategy pricingStrategy,
                Map<SeatType, Double> seatPriceMap) {
        this.id = id;
        this.movie = movie;
        this.theatre = theatre;
        this.screen = screen;
        this.startTime = startTime;
        this.pricingStrategy = pricingStrategy;
        this.seatMap = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();

        for (Seat seat : screen.getSeats()) {
            double price = seatPriceMap.get(seat.getType());
            Seat showSeat = new Seat(seat.getId(), seat.getSeatNumber(), seat.getType());
            showSeat.setPrice(price);
            seatMap.put(showSeat.getId(), showSeat);
        }
    }

    public List<Seat> getAvailableSeats() {
        return seatMap.values().stream()
                .filter(seat -> seat.getStatus().equals(SeatStatus.AVAILABLE))
                .filter(Seat::isLockExpired)
                .collect(Collectors.toList());
    }
}
