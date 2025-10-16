package com.problems.elevatorSystem;

public interface ElevatorState {
    void move(Elevator elevator);
    Direction getDirection();
    String getState();
}
