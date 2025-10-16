package com.problems.elevatorSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElevatorSystemDemo {
    public static void main(String[] args) {
        System.out.println("========== ELEVATOR SYSTEM STARTING ==========\n");

        int numberOfElevators=2;
        int capacity=8;
        int maxFloor = 15;
        int minFloor = 0;

        // schedule elevator strategy
        RequestSchedulingStrategy requestSchedulingStrategy = new LookSchedulingStrategy();

        // dispatch elevator strategy
        ElevatorDispatcherStrategy elevatorDispatcherStrategy = new OptimalDispatcherStrategy();

        ElevatorController elevatorController = new ElevatorController(numberOfElevators, minFloor, maxFloor, capacity,
                elevatorDispatcherStrategy, requestSchedulingStrategy);

        elevatorController.addObservers(List.of(new ElevatorDisplay()));



        ElevatorSystem elevatorSystem = new ElevatorSystem(elevatorController);


        try {
            elevatorSystem.requestElevator(5, Direction.DOWN);
            Thread.sleep(2000);
            elevatorSystem.selectFloor(10);
            Thread.sleep(2000);
            elevatorSystem.requestElevator(2, Direction.UP);
            Thread.sleep(2000);
            elevatorSystem.selectFloor(5);
            Thread.sleep(2000);
            elevatorSystem.requestElevator(3, Direction.UP);
            Thread.sleep(2000);
            elevatorSystem.selectFloor(10);
            Thread.sleep(2000);
            elevatorSystem.requestElevator(4, Direction.DOWN);
            Thread.sleep(2000);
            elevatorSystem.selectFloor(0);
            Thread.sleep(2000);
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("\n========== SHUTTING DOWN SYSTEM ==========");
            elevatorSystem.shutdown();
        }

    }
}
