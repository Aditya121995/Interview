package com.problems.bookMyShow;

import lombok.Getter;

@Getter
public class Movie {
    private final String id;
    private final String title;
    private final int durationInMinutes;

    public Movie(String id, String title, int durationInMinutes) {
        this.id = id;
        this.title = title;
        this.durationInMinutes = durationInMinutes;
    }
}
