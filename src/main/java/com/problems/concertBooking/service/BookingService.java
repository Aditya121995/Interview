package com.problems.concertBooking.service;

import com.problems.concertBooking.entity.Booking;
import com.problems.concertBooking.entity.Concert;
import com.problems.concertBooking.entity.Seat;
import com.problems.concertBooking.entity.User;
import com.problems.concertBooking.enums.SeatStatus;
import com.problems.concertBooking.repository.BookingRepository;
import com.problems.concertBooking.repository.ConcertRepository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final Object lock;
    private final BookingRepository bookingRepository;
    private final ConcertService concertService;
    private final UserService userService;

    public BookingService() {
        this.lock = new Object();
        this.bookingRepository = new BookingRepository();
        this.concertService = new ConcertService();
        this.userService = new UserService();
    }

    public Booking bookTickets(String concertId, String userId, List<Seat> seatList) {
        synchronized (lock) {
            // check seat availability
            for (Seat seat : seatList) {
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    throw new IllegalArgumentException("Seat is already booked");
                }
            }

            // check concert exists or not
            Concert concert = concertService.getConcert(concertId);
            if (concert == null) {
                throw new IllegalArgumentException("Concert not found");
            }

            // check user exists or not
            User user = userService.getUser(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            // mark all seats booked
            seatList.forEach(Seat :: reserve);

            // check if concert is sold out
            // if concert is sold out then add user to the waiting list

            // create booking
            Booking booking = new Booking(concert, user, seatList);
            bookingRepository.saveBooking(booking);

            // process paymenet
            processPayment(booking);

            // confirm booking
            booking.confirmBooking();

            // notify

            return booking;

        }
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        booking.cancelBooking();
    }

    private void processPayment(Booking booking) {
        // payment logic
    }

    public void addToWaitlist(Booking booking) {

    }
}
