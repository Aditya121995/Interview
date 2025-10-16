package com.problems.elevatorSystem;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Elevator {
    private final int id;
    private final int capacity;
    private final int maxFloor;
    private final int minFloor;

    @Setter
    private AtomicInteger currentFloor;

    @Setter
    private AtomicInteger currentLoad;

    private volatile ElevatorState currentState;

    @Setter
    private RequestSchedulingStrategy schedulingStrategy;

    private List<ElevatorObserver> observers;

    public Elevator(int id, int capacity, int maxFloor, int minFloor, RequestSchedulingStrategy schedulingStrategy) {
        this.id = id;
        this.capacity = capacity;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.currentFloor = new AtomicInteger(0);
        this.currentLoad = new AtomicInteger(0);
        this.currentState = new IdleState();
        this.schedulingStrategy = schedulingStrategy;
        this.observers = new CopyOnWriteArrayList<>();
    }

    public synchronized void addRequest(Request request) {
        int targetFloor = request.getTargetFloor();
        if (targetFloor > maxFloor ||  targetFloor < minFloor) {
            System.out.println("Invalid target floor : " + targetFloor);
            return;
        }

        schedulingStrategy.addRequest(this, request, currentState.getDirection());
        notifyObservers();
    }

    public synchronized void move() {
        currentState.move(this);
        notifyObservers();
    }

    public void addPassenger() {
        if(currentLoad.get() < capacity) {
            currentLoad.incrementAndGet();
        }
    }

    public void removePassenger() {
        if (currentLoad.get() > 0) {
            currentLoad.decrementAndGet();
        }
    }

    public boolean canAcceptPassenger() {
        return currentLoad.get() < capacity;
    }

    public void increaseFloor() {
        if (currentFloor.get() < maxFloor) {
            currentFloor.incrementAndGet();
        }
    }

    public void decreaseFloor() {
        if (currentFloor.get() > minFloor) {
            currentFloor.decrementAndGet();
        }
    }

    public synchronized void changeState(ElevatorState state) {
        this.currentState = state;
        notifyObservers();
    }

    private void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.update(this);
        }
    }

    public int getCurrentFloor() {
        return currentFloor.get();
    }

    public int getCurrentLoad() {
        return currentLoad.get();
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ElevatorObserver observer) {
        observers.remove(observer);
    }
}
