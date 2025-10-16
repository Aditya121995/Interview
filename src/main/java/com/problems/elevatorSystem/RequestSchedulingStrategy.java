package com.problems.elevatorSystem;

public interface RequestSchedulingStrategy {
    void addRequest(Elevator elevator, Request request, Direction elevatorDir);
    Integer getNextDestination(Elevator elevator, Direction elevatorDir);
    void removeFromDestination(Elevator elevator, int floor);
}
