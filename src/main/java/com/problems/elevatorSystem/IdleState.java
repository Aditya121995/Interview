package com.problems.elevatorSystem;

public class IdleState implements ElevatorState {
    @Override
    public void move(Elevator elevator) {
        Integer nextFloor = elevator.getSchedulingStrategy().getNextDestination(elevator, Direction.NONE);
        int currentFloor = elevator.getCurrentFloor();

//        System.out.println("elevator :: " + elevator.getId());
//        System.out.println("currentFloor :: " + currentFloor);
//        System.out.println("nextFloor :: " + nextFloor);
//        System.out.println("current Direction :: " + getDirection());

        if (nextFloor != null) {
            if (nextFloor <  currentFloor) {
                elevator.changeState(new MovingDownState());
            } else if (nextFloor > currentFloor) {
                elevator.changeState(new MovingUpState());
            } else {
                elevator.changeState(new DoorOpenState());
            }
        }

//        System.out.println("new Direction :: " + elevator.getCurrentState().getDirection());
    }

    @Override
    public Direction getDirection() {
        return Direction.NONE;
    }

    @Override
    public String getState() {
        return "IDLE";
    }
}
