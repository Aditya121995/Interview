package com.problems.kafkaQueueBasic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber {
    private final Subscriber subscriber;
    private final BlockingQueue<Message> messageQueue;
    private final AtomicInteger offset;
    private volatile boolean running;
    private Thread workerThread;

    public TopicSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
        this.messageQueue = new LinkedBlockingQueue<>();
        this.running = false;
    }

    public synchronized void consume(Message message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void startConsuming() {
        running = true;
        workerThread = new Thread(() -> {
            while (running) {
                try {
                    Message message = messageQueue.take();
                    subscriber.process(message);
                    offset.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        workerThread.start();
    }

    public void stopConsuming() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    public String getSubscriberId() {
        return subscriber.getId();
    }
}
