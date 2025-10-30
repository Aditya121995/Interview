package com.problems.BookMyShow;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingService implements PaymentObserver {
    private final Map<String, Booking> bookingMap;
    private final PaymentService paymentService;
    private static final int LOCK_DURATION_IN_MINUTES = 10;

    public BookingService(PaymentService paymentService) {
        this.bookingMap=new HashMap<>();
        this.paymentService=paymentService;
    }

    public Booking getBooking(String id){
        return bookingMap.get(id);
    }

    public Booking createBooking(User user, Show show, List<Seat> seats, PaymentMethod paymentMethod){
        ReentrantLock lock = show.getLock();
        lock.lock();
        try {

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
            System.out.println("seats :: " + seats);
            System.out.println("Total amount: " + totalAmount);

            // create booking
            Booking booking = new Booking(user, show, seats, totalAmount);
            Payment payment = paymentService.initiatePayment(user.getId(), booking.getBookingId(),
                    booking.getTotalAmount(), paymentMethod);
            booking.setPayment(payment);
            bookingMap.put(booking.getBookingId(), booking);

            System.out.println("Booking " + booking.getBookingId() + " has been created.");
            return booking;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onPaymentUpdate(Payment payment) {
        Booking booking = bookingMap.get(payment.getBookingId());
        System.out.println("Payment update ::"+ booking);
        if(booking==null ||  !booking.getStatus().equals(BookingStatus.PENDING)){
            System.out.println("Booking " + booking + " has been cancelled/confirmed.");
            return;
        }

        ReentrantLock lock = booking.getShow().getLock();
        lock.lock();

        try {
            PaymentStatus status = payment.getStatus();
            if (PaymentStatus.SUCCESS.equals(status)) {
                // book all seats
                for (Seat seat : booking.getSeats()) {
                    seat.book();
                }

                booking.setStatus(BookingStatus.CONFIRMED);
                System.out.println("Booking confirmed successfully for seats " + booking.getSeats());
            } else { // failed
                // release all reservations
                for (Seat seat : booking.getSeats()) {
                    seat.unlock();
                }

                booking.setStatus(BookingStatus.CANCELLED);
                System.out.println("Booking cancelled because of payment failure");
            }
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

    public List<Booking> getExpiredAllBookings() {
        return bookingMap.values().stream()
                .filter(Booking::isExpired)
                .collect(Collectors.toList());
    }
}
