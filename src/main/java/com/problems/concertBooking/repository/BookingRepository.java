package com.problems.concertBooking.repository;

import com.problems.concertBooking.entity.Booking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingRepository {
    private final Map<String, Booking> bookings;

    public BookingRepository() {
        this.bookings = new ConcurrentHashMap<>();
    }

    public Booking getBooking(String id) {
        return bookings.get(id);
    }

    public void saveBooking(Booking booking) {
        bookings.put(booking.getId(), booking);
    }
}
