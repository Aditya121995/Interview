package com.problems.kafkaQueueBasic;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message {
    private final String id;
    private final String content;
    private final long timestamp;

    public Message(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}
