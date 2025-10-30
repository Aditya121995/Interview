package com.problems.KafkaQueueManual;

public interface Subscriber {
    void processMessage(Message message);
    String getId();
}
