package com.problems.RideBookingSystem;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Passenger {
    private final String id;
    private final String name;
    private final String number;
    @Setter
    private Location currentLocation;

    public Passenger(String id, String name, String number, Location location) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.currentLocation = location;
    }
}
