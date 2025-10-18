package com.problems.bookMyShow;

import com.problems.carRentalSystem.PaymentMethod;

import java.util.*;
import java.util.stream.Collectors;

public class BookMyShowSystem {
    private final Map<String, Theatre> theatreMap;
    private final Map<String, City>  cityMap;
    private final Map<String, Show> showMap;
    private final Map<String, Movie> movieMap;
    private final Map<String, User> userMap;
    private final BookingManager bookingManager;

    public BookMyShowSystem() {
        this.theatreMap=new HashMap<>();
        this.cityMap=new HashMap<>();
        this.showMap=new HashMap<>();
        this.movieMap=new HashMap<>();
        this.userMap=new HashMap<>();
        this.bookingManager=new BookingManager();
    }

    public void addUser(User user){
        this.userMap.put(user.getId(), user);
    }

    public void addTheatre(Theatre theatre){
        this.theatreMap.put(theatre.getId(), theatre);
    }

    public void addShow(Show show){
        this.showMap.put(show.getId(), show);
    }

    public void addMovie(Movie movie){
        this.movieMap.put(movie.getId(), movie);
    }

    public void addCity(City city){
        this.cityMap.put(city.getId(), city);
    }

    public Booking createBooking(User user, Show show, List<Seat> seats){
        return bookingManager.createBooking(user, show, seats);
    }

    public void payForBooking(String bookingId, PaymentMethod paymentMethod){
        bookingManager.confirmBooking(bookingId, paymentMethod);
    }

    public void cancelBooking(String bookingId){
        bookingManager.cancelBooking(bookingId);
    }

    public List<Movie> getAllMoviesInCity(String cityId){
        List<Theatre> theatreList=getAllTheatresInCity(cityId);
        List<Movie> movieList=new ArrayList<>();
        for(Theatre theatre : theatreList){
            Set<Movie> movieInTheatre = showMap.values().stream()
                    .filter(show -> show.getTheatre().equals(theatre))
                    .map(Show::getMovie)
                    .collect(Collectors.toSet());

            movieList.addAll(movieInTheatre);
        }

        return movieList;
    }

    public List<Theatre> getAllTheatresShowingMovie(String movieId){
        Movie movie=movieMap.get(movieId);
        if(movie == null){
            System.out.println("Movie not found");
            return new ArrayList<>();
        }

        return showMap.values().stream()
                .filter(show -> show.getMovie().equals(movie))
                .map(Show::getTheatre)
                .collect(Collectors.toList());
    }

    public List<Theatre> getAllTheatresInCity(String cityId){
        City city = cityMap.get(cityId);
        if(city==null){
            System.out.println("City not found");
            return new ArrayList<>();
        }

        return theatreMap.values().stream()
                .filter(theatre -> theatre.getCity().equals(city))
                .collect(Collectors.toList());
    }

    public void shutDown(){
        bookingManager.shutdown();
    }
}
