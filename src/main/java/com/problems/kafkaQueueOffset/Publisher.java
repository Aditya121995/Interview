package com.problems.kafkaQueueOffset;

public class Publisher {
    private final String publisherId;

    public Publisher(String publisherId) {
        this.publisherId = publisherId;
    }

    public void publish(Topic topic, String content) {
        Message message = new Message(content);
        topic.publish(message);
        System.out.println(publisherId + " Published to topic " + topic + " with content " + content);
    }
}
