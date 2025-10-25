package com.problems.kafkaQueueManual;

import java.util.concurrent.atomic.AtomicInteger;

public class SubscriberWorker implements Runnable {
    private final TopicSubscriber topicSubscriber;
    private final Topic topic;

    public SubscriberWorker(TopicSubscriber topicSubscriber, Topic topic) {
        this.topicSubscriber = topicSubscriber;
        this.topic = topic;
    }

    @Override
    public void run() {
        synchronized (topicSubscriber) {
            while (topicSubscriber.getRunning().get()) {
                try {
                    int currentOffset = topicSubscriber.getOffset().get();
                    while (currentOffset >= topic.getMessages().size()) {
                        topicSubscriber.wait();
                        currentOffset =  topicSubscriber.getOffset().get();
                        if (topicSubscriber.getRunning().get()) {
                            break;
                        }
                    }

                    if (topicSubscriber.getRunning().get()) {
                        break;
                    }

                    Message message = topic.getMessages().get(currentOffset);
                    topicSubscriber.getSubscriber().processMessage(message);

                    topicSubscriber.getOffset().compareAndSet(currentOffset, currentOffset + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public void wakeup() {
        synchronized (topicSubscriber) {
            topicSubscriber.notify();
        }
    }

    public void resetOffset(int newOffset) {
        synchronized (topicSubscriber) {
            topicSubscriber.getOffset().set(newOffset);
            topicSubscriber.notify();
        }
    }

    public void shutdown() {
        synchronized (topicSubscriber) {
            topicSubscriber.getRunning().set(false);
            topicSubscriber.notify();
        }
    }
}
