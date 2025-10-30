package com.problems.BookMyShow;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMyShowDemo {
    public static void main(String[] args) throws InterruptedException {
        BookMyShowSystem bookMyShowSystem = new BookMyShowSystem();

        User u1 = new User("u1", "Aditya", "adi@gmail");
        User u2 = new User("u2", "Moksha", "moksha@gmail");
        bookMyShowSystem.addUser(u1);
        bookMyShowSystem.addUser(u2);

        City c1 = new City("BOM", "Bombay");
        City c2 = new City("BLR", "Bengaluru");
        City c3 = new City("DEL", "Delhi");
        bookMyShowSystem.addCity(c1);
        bookMyShowSystem.addCity(c2);
        bookMyShowSystem.addCity(c3);

        Theatre t1 = createTheatre("T1", "theatre2", c1);
        Theatre t2 = createTheatre("T2", "theatre2", c1);
        Theatre t3 = createTheatre("T3", "theatre3", c2);
        Theatre t4 = createTheatre("T4", "theatre4", c2);
        Theatre t5 = createTheatre("T5", "theatre5", c3);
        Theatre t6 = createTheatre("T6", "theatre6", c3);
        bookMyShowSystem.addTheatre(t1);
        bookMyShowSystem.addTheatre(t2);
        bookMyShowSystem.addTheatre(t3);
        bookMyShowSystem.addTheatre(t4);
        bookMyShowSystem.addTheatre(t5);
        bookMyShowSystem.addTheatre(t6);

        Movie m1 = new Movie("m1", "Bahubali", 180);
        Movie m2 = new Movie("m2", "Bahubali2", 180);
        Movie m3 = new Movie("m3", "War", 140);
        Movie m4 = new Movie("m4", "Dhoom", 150);
        bookMyShowSystem.addMovie(m1);
        bookMyShowSystem.addMovie(m2);
        bookMyShowSystem.addMovie(m3);
        bookMyShowSystem.addMovie(m4);

        Map<SeatType, Double> seatPriceMap = new HashMap<>();
        seatPriceMap.put(SeatType.NORMAL, 100.0);
        seatPriceMap.put(SeatType.PREMIUM, 200.0);
        seatPriceMap.put(SeatType.RECLINER, 300.0);

        Show s1 = new Show("sh1", m1, t1, t1.getScreens().get(0),
                LocalDateTime.now().plusHours(1), new WeekdayPricingStrategy(), seatPriceMap);
        Show s2 = new Show("sh2", m1, t4, t4.getScreens().get(0),
                LocalDateTime.now().plusHours(5), new WeekdayPricingStrategy(), seatPriceMap);
        Show s3 = new Show("sh3", m1, t3, t3.getScreens().get(0),
                LocalDateTime.now().plusHours(10), new WeekdayPricingStrategy(), seatPriceMap);
        Show s4 = new Show("sh4", m2, t2, t2.getScreens().get(0),
                LocalDateTime.now().plusHours(1), new WeekdayPricingStrategy(), seatPriceMap);
        Show s5 = new Show("sh5", m2, t2, t2.getScreens().get(0),
                LocalDateTime.now().plusHours(5), new WeekdayPricingStrategy(), seatPriceMap);
        Show s6 = new Show("sh6", m3, t2, t2.getScreens().get(0),
                LocalDateTime.now().plusHours(10), new WeekdayPricingStrategy(), seatPriceMap);

        bookMyShowSystem.addShow(s1);
        bookMyShowSystem.addShow(s2);
        bookMyShowSystem.addShow(s3);
        bookMyShowSystem.addShow(s4);
        bookMyShowSystem.addShow(s5);
        bookMyShowSystem.addShow(s6);

        System.out.println(bookMyShowSystem.getAllTheatresShowingMovie(m1.getId()));

        // book show
        List<Seat> selectedSeats = new ArrayList<>();
        selectedSeats.add(s1.getSeatMap().get("st1"));
        Booking bk1 = bookMyShowSystem.createBooking(u1, s1, selectedSeats, PaymentMethod.DEBIT_CARD);

        Booking bk2 = bookMyShowSystem.createBooking(u2, s1, selectedSeats, PaymentMethod.UPI);

        List<Seat> selectedSeats1 = new ArrayList<>();
        selectedSeats1.add(s2.getSeatMap().get("st2"));
        Booking bk3 = bookMyShowSystem.createBooking(u2, s1, selectedSeats1, PaymentMethod.UPI);
        bookMyShowSystem.payForBooking(bk1.getPayment().getPaymentId());
//        bookMyShowSystem.cancelBooking(bk1.getBookingId());

        Thread.sleep(20000);

        bookMyShowSystem.shutDown();
    }

    public static Theatre createTheatre(String t, String n, City c) {
        Theatre t1 = new Theatre(t, n, c);
        Screen t1s1 = new Screen("s1", "1");
        t1s1.addSeat(new Seat("st1", "1", SeatType.NORMAL));
        t1s1.addSeat(new Seat("st2", "2", SeatType.NORMAL));
        t1s1.addSeat(new Seat("st3", "3", SeatType.NORMAL));
        Screen t1s2 = new Screen("s1", "2");
        t1s2.addSeat(new Seat("st1", "1", SeatType.NORMAL));
        t1s2.addSeat(new Seat("st2", "2", SeatType.NORMAL));
        t1s2.addSeat(new Seat("st3", "3", SeatType.PREMIUM));
        Screen t1s3 = new Screen("s1", "3");
        t1s3.addSeat(new Seat("st1", "1", SeatType.NORMAL));
        t1s3.addSeat(new Seat("st2", "2", SeatType.PREMIUM));
        t1s3.addSeat(new Seat("st3", "3", SeatType.RECLINER));
        t1.addScreen(t1s1);
        t1.addScreen(t1s2);
        t1.addScreen(t1s3);

        return t1;
    }
}
