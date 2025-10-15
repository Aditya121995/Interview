package com.problems.parkingLotSystem;

import lombok.Getter;

@Getter
public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distanceFrom(Coordinate c) {
        double dx = this.x - c.x;
        double dy = this.y - c.y;
        return Math.sqrt(dx*dx + dy*dy);
    }


}
