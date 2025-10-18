package com.problems.bookMyShow;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Screen {
    private final String id;
    private final String number;
    private List<Seat> seats;

    public Screen(String id, String number) {
        this.id = id;
        this.number = number;
        this.seats=new ArrayList<>();
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
    }
}
