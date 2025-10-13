package com.problems.concertBooking.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Concert {
    private final String id;
    private final String artist;
    private final String venue;
    private final String title;
    private final LocalDateTime dateTime;
    private final boolean soldOut;
    private List<Seat> seats;

    public Concert(String artist,
                   String venue,
                   String title,
                   LocalDateTime dateTime,
                   List<Seat> seats) {
        if (artist == null) {
            throw new IllegalArgumentException("Artist cannot be null");
        }
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("Date Time cannot be null");
        }
        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("Seats cannot be null or empty");
        }

        this.id = UUID.randomUUID().toString();
        this.artist = artist;
        this.venue = venue;
        this.title = title;
        this.dateTime = dateTime;
        this.seats = seats;
        this.soldOut = false;
    }
}

