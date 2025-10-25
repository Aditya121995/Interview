package com.problems.kafkaQueueOffset;

public interface Subscriber {
    void process(Message message);
    String getId();
}
