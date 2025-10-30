package com.problems.KafkaQueueManual;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Broker {
    private static Broker instance;
    private final Map<String, Topic> topics;

    private Broker() {
        topics = new ConcurrentHashMap<>();
    }

    public static synchronized Broker getInstance() {
        if (instance == null) {
            instance = new Broker();
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

    public Topic getTopic(String topic) {
        return topics.get(topic);
    }

    public void subscribe(String topicName, Subscriber subscriber) {
        if (!topics.containsKey(topicName)) {
            System.out.println("Topic " + topicName + " does not exist");
            return;
        }

        Topic topic = topics.get(topicName);
        TopicSubscriber topicSubscriber = new TopicSubscriber(subscriber);
        topic.addSubscriber(topicSubscriber);
        System.out.println(subscriber.getId() + " subscribed to topic " + topicName);
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {
        if (!topics.containsKey(topicName)) {
            System.out.println("Topic " + topicName + " does not exist");
            return;
        }

        Topic topic = topics.get(topicName);
        TopicSubscriber topicSubscriber = topic.getTopicSubscriber(subscriber.getId());
        topic.removeSubscriber(topicSubscriber);
    }

    public void resetOffset(String topicName, Subscriber subscriber, int newOffset) {
        if (!topics.containsKey(topicName)) {
            System.out.println("Topic " + topicName + " does not exist");
            return;
        }

        Topic topic = topics.get(topicName);
        topic.resetOffset(subscriber.getId(), newOffset);
    }
}
