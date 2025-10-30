package com.problems.BookMyShow;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BookingExpirationService {
    private final BookingService bookingService;
    private final ScheduledExecutorService scheduler;
    private static final int CHECK_IN_SECONDS = 5;

    public BookingExpirationService(BookingService bookingService) {
        this.bookingService = bookingService;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startBookingExpiryChecker(){
        scheduler.scheduleAtFixedRate(this::checkAndExpireBookings,
                CHECK_IN_SECONDS, CHECK_IN_SECONDS, TimeUnit.SECONDS);
    }

    private void checkAndExpireBookings() {
        List<Booking> expiredBookings = bookingService.getExpiredAllBookings();

        System.out.println("Number of expired bookings: " + expiredBookings.size());

        if (expiredBookings.isEmpty()) {
            System.out.println("No bookings expired");
            return;
        }

        for (Booking booking : expiredBookings) {
            // release seats
            for (Seat seat : booking.getSeats()) {
                System.out.println(seat);
                seat.unlock();
            }

            booking.setStatus(BookingStatus.EXPIRED);
            if (booking.getPayment() != null) {
                booking.getPayment().setStatus(PaymentStatus.FAILED);
            }
        }
    }

    public void shutdown(){
        System.out.println("Shutting down Booking Manager");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
