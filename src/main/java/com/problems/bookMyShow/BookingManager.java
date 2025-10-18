package com.problems.bookMyShow;


import com.problems.carRentalSystem.PaymentMethod;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BookingManager {
    private final Map<String, Booking> bookingMap;
    private final ScheduledExecutorService scheduler;
    private static final int LOCK_DURATION_IN_MINUTES = 10;

    public BookingManager() {
        this.bookingMap=new HashMap<>();
        this.scheduler= Executors.newScheduledThreadPool(1);
        startLockExpiryChecker();
    }

    public Booking getBooking(String id){
        return bookingMap.get(id);
    }

    public Booking createBooking(User user, Show show, List<Seat> seats){
        ReentrantLock lock = show.getLock();
        lock.lock();
        try {
            // release expired ones first
            releaseExpiredLocks(show);

            // check if all the seats are avaialable
            for(Seat seat:seats){
                if (!seat.getStatus().equals(SeatStatus.AVAILABLE)){
                    System.out.println("Seat status is " + seat.getStatus() + ". Please select other seat.");
                    return null;
                }
            }

            // lock all the seats
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(LOCK_DURATION_IN_MINUTES);
            for(Seat seat:seats){
                seat.lock(user.getId(), expiry);
            }

            double totalAmount = show.getPricingStrategy().calculatePrice(seats);

            // create booking
            Booking booking = new Booking(user, show, seats, totalAmount);
            bookingMap.put(booking.getBookingId(), booking);

            System.out.println("Booking " + booking.getBookingId() + " has been created.");
            return booking;

        } finally {
            lock.unlock();
        }
    }

    public boolean confirmBooking(String bookingId, PaymentMethod paymentMethod) {
        Booking booking=bookingMap.get(bookingId);
        if(booking==null ||  !booking.getStatus().equals(BookingStatus.PENDING)){
            System.out.println("Booking " + booking + " has been cancelled/confirmed.");
            return false;
        }

        ReentrantLock lock = booking.getShow().getLock();
        lock.lock();
        try {


            // check all the seats are locked under same user
            for(Seat seat: booking.getSeats()){
                if (!seat.getLockedByUserId().equals(booking.getUser().getId())) {
                    return false;
                }
            }

            // make payment and confirm booking
            booking.confirm(paymentMethod);

            // mark seats booked
            if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
                for(Seat seat: booking.getSeats()){
                    seat.book();
                }

                return true;
            }

            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking=bookingMap.get(bookingId);
        if(booking==null || !booking.getStatus().equals(BookingStatus.PENDING)){
            System.out.println("Booking " + booking + " has been cancelled/confirmed.");
            return false;
        }

        ReentrantLock lock = booking.getShow().getLock();
        lock.lock();
        try {
            // check all the seats are locked under same user
            for(Seat seat: booking.getSeats()){
                if (!seat.getLockedByUserId().equals(booking.getUser().getId())) {
                    return false;
                }
            }

            for(Seat seat: booking.getSeats()){
                seat.unlock();
            }
            booking.cancel();

            return true;
        }  finally {
            lock.unlock();
        }
    }

    private void startLockExpiryChecker(){
        scheduler.scheduleAtFixedRate(() -> {
            for (Booking booking : bookingMap.values() ) {
                if (booking.getStatus().equals(BookingStatus.PENDING)) {
                    ReentrantLock lock = booking.getShow().getLock();
                    lock.lock();
                    try {

                        if(releaseExpiredLocks(booking.getShow())) {
                            booking.expire();
                        }
                    }  finally {
                        lock.unlock();
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private boolean releaseExpiredLocks(Show show){
        boolean releaseed=false;
        for (Seat seat : show.getSeatMap().values()) {
            if (seat.isLockExpired()) {
                seat.unlock();
                releaseed=true;
            }
        }

        return releaseed;
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
