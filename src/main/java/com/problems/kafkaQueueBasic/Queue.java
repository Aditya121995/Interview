package com.problems.kafkaQueueBasic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Queue {
    private static Queue instance;
    private final Map<String, Topic> topics;

    private Queue() {
        this.topics = new ConcurrentHashMap<>();
    }

    public static synchronized Queue getInstance() {
        if (instance == null) {
            instance = new Queue();
        }
        return instance;
    }

    public Topic createTopic(String topicName) {
        if (topics.containsKey(topicName)) {
            return topics.get(topicName);
        }

        Topic topic = new Topic(topicName);
        topics.put(topicName, topic);
        return topic;
    }

    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    public void subscribe(String topicName, Subscriber subscriber) {
        if (!topics.containsKey(topicName)) {
            System.out.println("Topic " + topicName + " does not exist");
            return;
        }
        Topic topic = topics.get(topicName);
        TopicSubscriber topicSubscriber = new TopicSubscriber(subscriber);
        topic.addSubscriber(topicSubscriber);
        topicSubscriber.startConsuming();
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {
        if (!topics.containsKey(topicName)) {
            System.out.println("Topic " + topicName + " does not exist");
            return;
        }
        Topic topic = topics.get(topicName);
        TopicSubscriber topicSubscriber = topic.getTopicSubscriber(subscriber.getId());
        if (topicSubscriber == null) {
            System.out.println("Subscriber " + subscriber.getId() + " does not subscribed topic " + topic.getTopicName());
            return;
        }

        topic.removeSubscriber(topicSubscriber);
        topicSubscriber.stopConsuming();

    }
}
