package com.problems.elevatorSystem;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Request {
    private final int targetFloor;
    private final Direction direction;
    private final RequestType requestType;

    public Request(int targetFloor, Direction direction, RequestType requestType) {
        this.targetFloor = targetFloor;
        this.direction = direction;
        this.requestType = requestType;
    }
}
