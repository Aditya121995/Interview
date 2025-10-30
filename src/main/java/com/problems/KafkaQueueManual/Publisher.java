package com.problems.KafkaQueueManual;


public class Publisher {
    private final String publisherId;

    public Publisher(String publisherId) {
        this.publisherId = publisherId;
    }

    public void publish(Topic topic, String content) {
        Message message = new Message(content);
        new Thread(() -> topic.publish(message)).start();
        System.out.println(publisherId + " Published to topic " + topic + " with content " + content);
    }
}
