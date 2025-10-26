package com.problems.asyncTaskScheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final WorkerThread[] workerThreads;
    private final AtomicBoolean isShutDown;

    public CustomThreadPool(int threadPoolSize) {
        taskQueue = new LinkedBlockingQueue<>();
        workerThreads = new WorkerThread[threadPoolSize];
        isShutDown = new AtomicBoolean(false);

        for (int i = 0; i < threadPoolSize; i++) {
            workerThreads[i] = new WorkerThread("Worker-" + i);
            workerThreads[i].start();
        }
    }

    public void submit(Runnable task) {
        if (isShutDown.get()) {
            System.out.println("Thread pool is shutting down");
            return;
        }

        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Submitted task");
    }

    public void shutdown() {
        isShutDown.set(true);
    }

    public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        while (!taskQueue.isEmpty()) {
            Thread.sleep(unit.toMillis(timeout));
        }

        for (WorkerThread worker : workerThreads) {
            worker.interrupt();
        }

        for (WorkerThread worker : workerThreads) {
            worker.join();
        }
    }


    private class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!isShutDown.get() || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);

                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            System.out.println("Error executing the task in " + getName()
                                    + " : " + e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    if (isShutDown.get()) {
                        break;
                    }
                }
            }
        }
    }

}
