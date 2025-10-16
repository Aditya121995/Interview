package com.problems.elevatorSystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LookSchedulingStrategy implements RequestSchedulingStrategy {
    private final Map<Integer, ElevatorData> elevatorQueuesMap;

    public LookSchedulingStrategy() {
        elevatorQueuesMap = new ConcurrentHashMap<>();
    }

    private static class ElevatorData {
        final PriorityQueue<Integer> upQueue = new PriorityQueue<>();
        final PriorityQueue<Integer> downQueue = new PriorityQueue<>(Collections.reverseOrder());
        final List<Integer> pendingFloors = new ArrayList<>();
        final ReentrantLock lock = new ReentrantLock();
    }

    @Override
    public void addRequest(Elevator elevator, Request request, Direction elevatorDir) {
        ElevatorData elevatorQueues = elevatorQueuesMap.computeIfAbsent(elevator.getId(),
                k -> new ElevatorData());

        Direction requestDir = request.getDirection();
        int targetFloor = request.getTargetFloor();
        int currentFloor = elevator.getCurrentFloor();

        elevatorQueues.lock.lock();
        try {
            // Internal request
            if(request.getRequestType().equals(RequestType.INTERNAL)){
                if (currentFloor <= targetFloor) {
                    elevatorQueues.upQueue.add(targetFloor);
                } else {
                    elevatorQueues.downQueue.add(targetFloor);
                }
                return;
            }

            // External request
            if (requestDir == Direction.UP) {
                if (elevatorDir == Direction.UP) {
                    if (currentFloor <= targetFloor) {
                        elevatorQueues.upQueue.add(targetFloor);
                    }  else {
                        elevatorQueues.pendingFloors.add(currentFloor);
                    }
                } else if (elevatorDir == Direction.DOWN) {
                    elevatorQueues.upQueue.add(targetFloor);
                } else {
                    if(currentFloor <= targetFloor) {
                        elevatorQueues.upQueue.add(targetFloor);
                    } else  {
                        elevatorQueues.downQueue.add(targetFloor);
                    }
                }
            } else if (requestDir == Direction.DOWN) {
                if (elevatorDir == Direction.DOWN) {
                    if (currentFloor >= targetFloor) {
                        elevatorQueues.downQueue.add(targetFloor);
                    }   else {
                        elevatorQueues.pendingFloors.add(currentFloor);
                    }
                } else if (elevatorDir == Direction.UP)  {
                    elevatorQueues.downQueue.add(targetFloor);
                } else {
                    if(currentFloor <= targetFloor) {
                        elevatorQueues.upQueue.add(targetFloor);
                    } else  {
                        elevatorQueues.downQueue.add(targetFloor);
                    }
                }
            }
        } finally {
            elevatorQueues.lock.unlock();
        }

    }

    @Override
    public Integer getNextDestination(Elevator elevator, Direction elevatorDir) {
        ElevatorData elevatorQueues = elevatorQueuesMap.computeIfAbsent(elevator.getId(),
                k -> new ElevatorData());

        elevatorQueues.lock.lock();
//        System.out.println("elevatorDir :: " + elevatorDir);
        try {
            if (elevatorDir == Direction.UP) {
                if (elevatorQueues.upQueue.isEmpty()) {
                    // we need to check if we move down from here
                    if (!elevatorQueues.pendingFloors.isEmpty()) {
                        elevatorQueues.upQueue.addAll(elevatorQueues.pendingFloors);
                        elevatorQueues.pendingFloors.clear();
                    }

//                    System.out.println("UpQueue is empty");

                    return elevatorQueues.downQueue.peek();
                } else {
                    return elevatorQueues.upQueue.peek();
                }
            }  else if (elevatorDir == Direction.DOWN) {
                if (elevatorQueues.downQueue.isEmpty()) {
                    if (!elevatorQueues.pendingFloors.isEmpty()) {
                        elevatorQueues.downQueue.addAll(elevatorQueues.pendingFloors);
                        elevatorQueues.pendingFloors.clear();
                    }

                    return elevatorQueues.upQueue.peek();
                } else  {
                    return elevatorQueues.downQueue.peek();
                }
            } else {
                if (!elevatorQueues.upQueue.isEmpty()) {
                    return elevatorQueues.upQueue.peek();
                }
                if (!elevatorQueues.downQueue.isEmpty()) {
                    return elevatorQueues.downQueue.peek();
                }
            }
        } finally {
            elevatorQueues.lock.unlock();
        }

        return null;
    }

    @Override
    public void removeFromDestination(Elevator elevator, int floor) {
        ElevatorData elevatorQueues = elevatorQueuesMap.get(elevator.getId());
        elevatorQueues.lock.lock();
        try {
            elevatorQueues.downQueue.remove(floor);
            elevatorQueues.upQueue.remove(floor);
        } finally {
            elevatorQueues.lock.unlock();
        }
    }
}
