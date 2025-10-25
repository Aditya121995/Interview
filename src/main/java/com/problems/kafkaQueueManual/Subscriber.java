package com.problems.kafkaQueueManual;

public interface Subscriber {
    void processMessage(Message message);
    String getId();
}
