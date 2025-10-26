package com.problems.asyncTaskScheduler;

import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        TaskScheduler taskScheduler = TaskScheduler.getInstance(4);

        // create tasks A and B with no dependencies
        Task taskA = new Task("A", () -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task A is processing....");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, null);

        Task taskB = new Task("B", () -> {
            try {
                Thread.sleep(800);
                System.out.println("Task B is processing....");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, null);

        // create tasks C depends on A
        Task taskC = new Task("C", () -> {
            try {
                Thread.sleep(500);
                System.out.println("Task C is processing....");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, List.of("A"));

        // create tasks D depends on A and B
        Task taskD = new Task("D", () -> {
            try {
                Thread.sleep(600);
                System.out.println("Task D is processing....");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, List.of("A", "B"));

        // create tasks E depends on C and D
        Task taskE = new Task("E", () -> {
            try {
                Thread.sleep(600);
                System.out.println("Task E is processing....");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, List.of("C", "D"));

        taskScheduler.addTask(taskA);
        taskScheduler.addTask(taskB);
        taskScheduler.addTask(taskC);
        taskScheduler.addTask(taskD);
        taskScheduler.addTask(taskE);

        System.out.println("Starting the execution...");
        taskScheduler.runTasks();
    }
}
