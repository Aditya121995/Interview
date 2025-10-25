package com.problems.kafkaQueueOffset;

public class SleepingSubscriber implements Subscriber {
    private final String subscriberId;
    private final int sleepTime;

    public SleepingSubscriber(String subscriberId, int sleepTime) {
        this.subscriberId = subscriberId;
        this.sleepTime = sleepTime;
    }

    @Override
    public void process(Message message) {
        System.out.println(subscriberId + "Started processing message " + message.getContent());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(subscriberId + "Finished processing message " + message.getContent());
    }

    @Override
    public String getId() {
        return subscriberId;
    }
}
