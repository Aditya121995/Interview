package com.problems.concertBooking.service;

import com.problems.concertBooking.entity.Concert;
import com.problems.concertBooking.repository.ConcertRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ConcertService {
    private final ConcertRepository concertRepository;

    public ConcertService() {
        this.concertRepository = new ConcertRepository();
    }

    public void addConcert(Concert concert) {
        concertRepository.saveConcert(concert);
    }

    public List<Concert> getAllAvailableConcerts() {
        List<Concert> concerts = concertRepository.getConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No available concerts");
            return Collections.emptyList();
        }
        return concerts;
    }

    public Concert getConcert(String concertId) {
        Optional<Concert> concertOpt = concertRepository.getConcert(concertId);
        if (concertOpt.isPresent()) {
            return concertOpt.get();
        }
        System.out.println("Concert not found");
        return null;
    }

    public List<Concert> searchConcert(String artist, String venue, LocalDateTime dateTime) {
        List<Concert> concerts = concertRepository.getConcerts(artist, venue, dateTime);
        if (concerts.isEmpty()) {
            System.out.println("No available concerts for the given search");
            return Collections.emptyList();
        }

        return concerts;
    }

}
