package com.problems.kafkaQueueBasic;

import lombok.Getter;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class Topic {
    private final String topicId;
    private final String topicName;
    private final Set<TopicSubscriber> subscriberList;

    public Topic(String topicName) {
        this.topicId = UUID.randomUUID().toString();
        this.topicName = topicName;
        this.subscriberList = new CopyOnWriteArraySet<>();
    }

    public void addSubscriber(TopicSubscriber subscriber) {
        this.subscriberList.add(subscriber);
    }

    public void removeSubscriber(TopicSubscriber subscriber) {
        this.subscriberList.remove(subscriber);
    }

    public TopicSubscriber getTopicSubscriber(String subscriberId) {
        return subscriberList.stream()
                .filter(t -> t.getSubscriberId().equals(subscriberId))
                .findFirst().orElse(null);
    }

    public void publish(Message message) {
        for (TopicSubscriber subscriber : this.subscriberList) {
            subscriber.consume(message);
        }
    }
}
