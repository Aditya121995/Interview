package com.problems.kafkaQueueOffset;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber {
    private final Subscriber subscriber;
    private final BlockingQueue<Message> messageQueue;
    private final AtomicInteger offset;
    private volatile boolean running;
    private Thread woekerThread;

    public TopicSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.offset = new AtomicInteger(0);
        this.running = false;
    }

    public void consume(Message message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void startConsuming() {
        running = true;
        woekerThread = new Thread(() -> {
            while (running) {
                try {
                    Message message = messageQueue.take();
                    subscriber.process(message);
                    offset.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Subscriber-Thread-" + subscriber.getId());
        woekerThread.start();
    }

    public void stopConsuming() {
        running = false;
        if (woekerThread != null) {
            woekerThread.interrupt();
        }
    }

    public String getSubscriberId() {
        return subscriber.getId();
    }

    public int getOffset() {
        return offset.get();
    }
}
