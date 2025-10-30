package com.problems.KafkaQueueManual;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class Topic {
    private final String topicId;
    private final String topicName;
    private final Set<TopicSubscriber> subscribers;
    private final List<Message> messages;
    private final Map<String, SubscriberWorker> subscriberWorkers;

    public Topic(String topicName) {
        this.topicId = UUID.randomUUID().toString();
        this.topicName = topicName;
        this.subscribers = new CopyOnWriteArraySet<>();
        this.messages = new CopyOnWriteArrayList<>();
        this.subscriberWorkers = new ConcurrentHashMap<>();
    }

    public void addSubscriber(TopicSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(TopicSubscriber subscriber) {
        subscribers.remove(subscriber);
        SubscriberWorker subscriberWorker = subscriberWorkers.getOrDefault(subscriber.getSubscriber().getId(), null);
        if (subscriberWorker != null) {
            subscriberWorker.shutdown();
            System.out.println("----");
        }
    }

    public TopicSubscriber getTopicSubscriber(String subscriberId) {
        return subscribers.stream()
                .filter(sub -> sub.getSubscriber().getId().equals(subscriberId))
                .findFirst().orElse(null);
    }

    public void publish(Message message) {
        messages.add(message);
        for (TopicSubscriber subscriber : subscribers) {
            startSubscriberWorker(subscriber);
        }
    }

    private void startSubscriberWorker(TopicSubscriber subscriber) {
        String subsriberId = subscriber.getSubscriber().getId();

        SubscriberWorker subscriberWorker = subscriberWorkers.computeIfAbsent(subsriberId, key -> {
            SubscriberWorker newWorker = new SubscriberWorker(subscriber, this);
            Thread thread = new Thread(newWorker, "SubscriberWorker-" + subsriberId);
            thread.start();
            return newWorker;
        });

        subscriberWorker.wakeup();
    }

    public void resetOffset(String subscriberId, int newOffset) {
        SubscriberWorker subscriberWorker = subscriberWorkers.get(subscriberId);
        if (subscriberWorker != null) {
            subscriberWorker.resetOffset(newOffset);
        }
    }
}
