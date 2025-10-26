package com.problems.asyncTaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

public class TaskScheduler {
    private static TaskScheduler instance;
    private final Map<String, Task> tasks;
    private final Map<String, List<String>> dependencyGraph;
    private final Map<String, Integer> indegree;
    private final Queue<Task> readyQueue;
    private final CustomThreadPool executor;

    private TaskScheduler(int threadPoolSize) {
        tasks = new ConcurrentHashMap<>();
        dependencyGraph = new ConcurrentHashMap<>();
        indegree = new ConcurrentHashMap<>();
        readyQueue = new ConcurrentLinkedQueue<>();
        executor = new CustomThreadPool(threadPoolSize);
    }

    public static synchronized TaskScheduler getInstance(int threadPoolSize) {
        if (instance == null) {
            instance = new TaskScheduler(threadPoolSize);
        }

        return instance;
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(),  task);
        indegree.put(task.getTaskId(), task.getDependencies().size());

        // create a reverse graph (dependency graph)
        for (String dependency : task.getDependencies()) {
            dependencyGraph.computeIfAbsent(dependency, k-> new ArrayList<>())
                    .add(task.getTaskId());
        }

        if (task.getDependencies().isEmpty()) {
            readyQueue.offer(task);
        }
    }

    public void runTasks() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(tasks.size());

        while (!readyQueue.isEmpty() || hasIncompleteTasks()) {
            while (!readyQueue.isEmpty()) {
                Task task = readyQueue.poll();

                executor.submit(() -> {
                    try {
                        task.execute();
                        onTaskComplete(task);
                    } finally {
                        latch.countDown();
                    }
                });
            }

//            Thread.sleep(100);
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All tasks are completed !!");
    }

    private synchronized void onTaskComplete(Task task) {
        String taskId = task.getTaskId();
        List<String> dependentTasks = dependencyGraph.getOrDefault(taskId, new ArrayList<>());

        for (String dependentTask : dependentTasks) {
            int currentIndegree = indegree.get(dependentTask);
            indegree.put(dependentTask, currentIndegree - 1);

            if (indegree.get(dependentTask) == 0) {
                readyQueue.offer(tasks.get(dependentTask));
            }
        }
    }

    private boolean hasIncompleteTasks() {
        return tasks.values().stream().anyMatch(task -> !task.isCompleted());
    }
}
