package com.problems.kafkaQueueBasic;

public class SleepingSubscriber implements Subscriber {
    private final String id;
    private final int sleepTimeInMillis;

    public SleepingSubscriber(String id, int sleepTimeInMillis) {
        this.id = id;
        this.sleepTimeInMillis = sleepTimeInMillis;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void process(Message message) {
        System.out.println("Subscriber: " + id + " Started consuming message: " + message.getContent());
        try {
            Thread.sleep(sleepTimeInMillis);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted : " + e);
            Thread.currentThread().interrupt();
        }
        System.out.println("Subscriber: " + id + " Finished consuming message: " + message.getContent());
    }
}
