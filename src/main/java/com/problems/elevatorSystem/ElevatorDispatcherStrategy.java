package com.problems.elevatorSystem;

import java.util.List;

public interface ElevatorDispatcherStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}
