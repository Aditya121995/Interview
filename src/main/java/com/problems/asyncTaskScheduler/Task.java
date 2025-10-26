package com.problems.asyncTaskScheduler;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Task {
    private final String taskId;
    private final Runnable logic;
    private final List<String> dependencies;
    @Setter
    private volatile boolean completed;

    public Task(String taskId, Runnable logic, List<String> dependencies) {
        this.taskId = taskId;
        this.logic = logic;
        this.dependencies = dependencies == null ? new ArrayList<>() : dependencies;
        this.completed = false;
    }

    public void execute() {
        System.out.println("Executing task " + taskId);
        logic.run();
        this.completed = true;
        System.out.println("Task " + taskId + " has been executed");
    }
}
