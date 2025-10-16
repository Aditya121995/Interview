package com.problems.elevatorSystem;

public class ElevatorDisplay implements ElevatorObserver {
    @Override
    public void update(Elevator elevator) {
        System.out.println("[DISPLAY] Elevator " + elevator.getId() +
                " | Current Floor: " + elevator.getCurrentFloor() +
                " | Direction: " + elevator.getCurrentState().getDirection() +
                " | CurrentState: " + elevator.getCurrentState().getState());
    }
}
