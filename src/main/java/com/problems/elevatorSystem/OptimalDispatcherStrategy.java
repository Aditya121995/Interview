package com.problems.elevatorSystem;

import java.util.List;

public class OptimalDispatcherStrategy implements ElevatorDispatcherStrategy {
    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {
        Elevator bestElevator=null;
        double minCost=Double.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (!elevator.canAcceptPassenger()) {
                continue;
            }

            int cost = calculateCost(elevator, request);
            if (cost < minCost) {
                minCost=cost;
                bestElevator=elevator;
            }
        }
        return bestElevator;
    }

    private int calculateCost(Elevator elevator, Request request) {
        int currentFloor = elevator.getCurrentFloor();
        int targetFloor = request.getTargetFloor();

        Direction elevatorDirection = elevator.getCurrentState().getDirection();
        Direction targetDirection = request.getDirection();

        // if moving in same direction as requested
        if (elevatorDirection == targetDirection && !elevatorDirection.equals(Direction.NONE)) {
            if (elevatorDirection.equals(Direction.UP) && targetFloor >= currentFloor) {
                return targetFloor - currentFloor;
            }
            if (elevatorDirection.equals(Direction.DOWN) && targetFloor <= currentFloor) {
                return currentFloor - targetFloor;
            }
        }

        // if in idle state
        if(elevatorDirection.equals(Direction.NONE)) {
            return Math.abs(targetFloor - currentFloor) + 10;
        }

        // if in opposite direction
        return Math.abs(targetFloor - currentFloor) + 100;
    }
}
