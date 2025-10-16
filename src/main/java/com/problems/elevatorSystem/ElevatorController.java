package com.problems.elevatorSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final ElevatorDispatcherStrategy elevatorDispatcherStrategy;
    private final BlockingQueue<Request> requestQueue;
    private final ExecutorService executor;
    private final AtomicBoolean running;

    public ElevatorController(int numElevators, int minFloor, int maxFloor,
                              int capacity, ElevatorDispatcherStrategy elevatorDispatcherStrategy,
                              RequestSchedulingStrategy requestSchedulingStrategy) {
        this.elevatorDispatcherStrategy=elevatorDispatcherStrategy;
        this.requestQueue=new LinkedBlockingQueue<>();
        this.executor= Executors.newFixedThreadPool(numElevators+1);
        this.running=new AtomicBoolean(true);
        this.elevators = new ArrayList<>();
        for(int i=0; i<numElevators; i++){
            elevators.add(new Elevator(i, capacity, maxFloor, minFloor, requestSchedulingStrategy));
        }

        startElevator();
        startRequestProcessor();
    }

    public void submitRequest(Request request) {
        requestQueue.add(request);
        System.out.println("Request submitted: " + request);
    }

    public void shutdown() {
        running.set(false);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void startElevator() {
        for (Elevator elevator : elevators) {
            executor.submit(() -> {
                while (running.get() && !Thread.currentThread().isInterrupted()) {
                    try {
                        elevator.move();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    private void startRequestProcessor() {
        executor.submit(() -> {
            while (running.get() &&  !Thread.currentThread().isInterrupted()) {
                try {
                    Request request = requestQueue.poll(5, TimeUnit.SECONDS);
                    if (request != null) {
                        processRequest(request);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void processRequest(Request request) {
        Elevator selectedElevator = elevatorDispatcherStrategy.selectElevator(elevators, request);

        if (selectedElevator != null) {
            selectedElevator.addRequest(request);
            System.out.println("Added request to elevator: " + selectedElevator.getId() + ", currently at floor: " +
                    selectedElevator.getCurrentFloor());
        } else {
            System.out.println("No elevator is available to take the request");
            try {
                Thread.sleep(500);
                requestQueue.add(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addObservers(List<ElevatorObserver> observers) {
        for (Elevator elevator : elevators) {
            for(ElevatorObserver observer : observers){
                elevator.addObserver(observer);
            }
        }
    }
}
