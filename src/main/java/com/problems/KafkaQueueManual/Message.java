package com.problems.KafkaQueueManual;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message {
    private final String messageId;
    private final String content;
    private final long timestamp;

    public Message(String content) {
        this.content=content;
        this.timestamp=System.currentTimeMillis();
        this.messageId= UUID.randomUUID().toString();
    }
}
