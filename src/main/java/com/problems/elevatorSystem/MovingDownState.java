package com.problems.elevatorSystem;

public class MovingDownState implements ElevatorState {

    @Override
    public void move(Elevator elevator) {
        Integer nextFloor = elevator.getSchedulingStrategy().getNextDestination(elevator, Direction.DOWN);
        int currentFloor = elevator.getCurrentFloor();

//        System.out.println("elevator :: " + elevator.getId());
//        System.out.println("currentFloor :: " + currentFloor);
//        System.out.println("nextFloor :: " + nextFloor);
//        System.out.println("current Direction :: " + getDirection());

        if(nextFloor != null) {
            if (nextFloor <  currentFloor) {
                elevator.decreaseFloor();
            } else if (nextFloor > currentFloor) {
                elevator.changeState(new MovingUpState());
            } else {
                elevator.changeState(new DoorOpenState());
            }
        } else {
            elevator.changeState(new IdleState());
        }

//        System.out.println("new Direction :: " + elevator.getCurrentState().getDirection());
    }

    @Override
    public Direction getDirection() {
        return Direction.DOWN;
    }

    @Override
    public String getState() {
        return "MOVING_DOWN";
    }
}
