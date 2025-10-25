package com.problems.kafkaQueueBasic;

public interface Subscriber {
    String getId();
    void process(Message message);
}
