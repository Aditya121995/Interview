package com.problems.elevatorSystem;

public class DoorOpenState implements ElevatorState {

    @Override
    public void move(Elevator elevator) {
        try {
            elevator.addPassenger();
            Thread.sleep(2000);

            System.out.println("Elevator " + elevator.getId() +
                    " - Doors CLOSING at floor " + elevator.getCurrentFloor());

            elevator.getSchedulingStrategy().removeFromDestination(elevator, elevator.getCurrentFloor());
            elevator.removePassenger();


            Integer nextFloor = elevator.getSchedulingStrategy().getNextDestination(elevator, Direction.NONE);
            int currentFloor = elevator.getCurrentFloor();

//            System.out.println("elevator :: " + elevator.getId());
//            System.out.println("currentFloor :: " + currentFloor);
//            System.out.println("nextFloor :: " + nextFloor);
//            System.out.println("current Direction :: " + getDirection());

            if (nextFloor != null) {
                if (nextFloor <  currentFloor) {
                    elevator.changeState(new MovingDownState());
                } else if (nextFloor > currentFloor) {
                    elevator.changeState(new MovingUpState());
                } else {
                    elevator.changeState(new IdleState());
                }
            } else {
                elevator.changeState(new IdleState());
            }

//            System.out.println("new Direction :: " + elevator.getCurrentState().getDirection());

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Direction getDirection() {
        return Direction.NONE;
    }

    @Override
    public String getState() {
        return "Doors_Open";
    }
}
