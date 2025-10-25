package com.problems.kafkaQueueOffset;

import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class Topic {
    private final String topicId;
    private final String topicName;
    private final Set<TopicSubscriber> topicSubscribers;
    private final List<Message> messageStore;

    public Topic(String topicName) {
        this.topicId = UUID.randomUUID().toString();
        this.topicName = topicName;
        this.topicSubscribers = new CopyOnWriteArraySet<>();
        this.messageStore = new CopyOnWriteArrayList<>();
    }

    public void addSubscriber(TopicSubscriber topicSubscriber) {
        this.topicSubscribers.add(topicSubscriber);
    }

    public void removeSubscriber(TopicSubscriber topicSubscriber) {
        this.topicSubscribers.remove(topicSubscriber);
    }

    public TopicSubscriber getSubscriber(String subscriberId) {
        return topicSubscribers.stream()
                .filter(sub -> sub.getSubscriberId().equals(subscriberId))
                .findFirst().orElse(null);
    }

    public void publish(Message message) {
        messageStore.add(message);
        for (TopicSubscriber topicSubscriber : topicSubscribers) {
            topicSubscriber.consume(message);
        }
    }

    public void resetOffset(String subscriberId, int newOffset) {
        TopicSubscriber topicSubscriber = getSubscriber(subscriberId);
        if (topicSubscriber == null) {
            System.out.println("Subscriber: " + subscriberId + " not found for topic: " + topicName);
            return;
        }

        if (newOffset < 0 || messageStore.size() <= newOffset) {
            System.out.println("Invalid offset: " + newOffset);
            return;
        }

        for (int i=newOffset; i<messageStore.size(); i++) {
            topicSubscriber.consume(messageStore.get(i));
        }
    }

}
