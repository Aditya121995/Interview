package com.problems.parkingLotSystem;

import lombok.Getter;

@Getter
public abstract class Gate {
    private final String gateId;
    private final int gateNumber;
    private final Coordinate coordinate;

    public Gate(String gateId, int gateNumber,  Coordinate coordinate) {
        this.gateId = gateId;
        this.gateNumber = gateNumber;
        this.coordinate = coordinate;
    }
}
