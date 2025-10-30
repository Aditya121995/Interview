package com.problems.KafkaQueueManual;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class TopicSubscriber {
    private final Subscriber subscriber;
    private final AtomicInteger offset;
    private final AtomicBoolean running;

    public TopicSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
        this.running = new AtomicBoolean(true);
    }
}
