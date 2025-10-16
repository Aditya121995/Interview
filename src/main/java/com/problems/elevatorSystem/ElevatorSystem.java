package com.problems.elevatorSystem;

public class ElevatorSystem {
    private final ElevatorController elevatorController;

    public ElevatorSystem(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    public void requestElevator(int floor, Direction direction) {
        Request request = new Request(floor, direction, RequestType.EXTERNAL);
        elevatorController.submitRequest(request);
    }

    public void selectFloor(int floor) {
        Request request = new Request(floor, Direction.NONE, RequestType.INTERNAL);
        elevatorController.submitRequest(request);
    }

    public void shutdown() {
        elevatorController.shutdown();
    }
}
