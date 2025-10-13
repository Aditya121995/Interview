package com.problems.concertBooking.repository;

import com.problems.concertBooking.entity.Concert;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConcertRepository {
    private final Map<String, Concert> concerts;

    public ConcertRepository() {
        concerts = new ConcurrentHashMap<>();
    }

    public Optional<Concert> getConcert(String id) {
        return Optional.ofNullable(concerts.get(id));
    }

    public List<Concert> getConcerts() {
        return concerts.values().stream()
                .filter(concert -> !concert.isSoldOut())
                .collect(Collectors.toList());
    }

    public List<Concert> getConcerts(String artist, String venue, LocalDateTime date) {
        return concerts.values().stream()
                .filter(concert -> {
                    if (artist == null || artist.isEmpty()) {
                        return false;
                    }
                    return artist.equalsIgnoreCase(concert.getArtist());
                })
                .filter(concert -> {
                    if (venue == null || venue.isEmpty()) {
                        return false;
                    }
                    return venue.equalsIgnoreCase(concert.getVenue());
                })
                .filter(concert -> {
                    if (date == null) {
                        return false;
                    }
                    return date.equals(concert.getDateTime());
                })
                .collect(Collectors.toList());
    }

    public void saveConcert(Concert concert) {
        concerts.put(concert.getId(), concert);
    }


}
