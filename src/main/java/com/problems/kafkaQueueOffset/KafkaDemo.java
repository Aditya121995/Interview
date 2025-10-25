package com.problems.kafkaQueueOffset;


public class KafkaDemo {
    public static void main(String[] args) {
        Queue queue = Queue.getInstance();

        // create topics
        Topic topic1 = queue.createTopic("topic1");
        Topic topic2 = queue.createTopic("topic2");

        // create subscribers
        Subscriber subscriber1 = new SleepingSubscriber("s1", 10000);
        Subscriber subscriber2 = new SleepingSubscriber("s2", 5000);
        Subscriber subscriber3 = new SleepingSubscriber("s3", 2000);

        // subscribe to topics
        queue.subscribe("topic1", subscriber1);
        queue.subscribe("topic1", subscriber2);
        queue.subscribe("topic2", subscriber3);

        // create publishers
        Publisher publisher1 = new Publisher("p1");
        Publisher publisher2 = new Publisher("p2");

        // publish messages
        publisher1.publish(topic1, "1 from p1");
        publisher1.publish(topic1, "2 from p1");
        publisher2.publish(topic2, "1 from p2");

        try {
            Thread.sleep(21000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        queue.resetOffset("topic1", subscriber2, 0);

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // stop subscribers
        queue.unsubscribe("topic1", subscriber1);
        queue.unsubscribe("topic1", subscriber2);
        queue.unsubscribe("topic2", subscriber3);
    }
}
