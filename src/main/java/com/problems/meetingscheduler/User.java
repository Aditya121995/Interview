package com.problems.meetingscheduler;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class User implements Observer {
    private final String userId;
    private final String name;
    private final String email;
    private final List<NotificationStrategy> notificationStrategy;

    public User(String userId, String name, String email, List<NotificationStrategy> notificationStrategy) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.notificationStrategy = notificationStrategy;
    }

    public synchronized void addNotificationStrategy(NotificationStrategy notificationStrategy) {
        this.notificationStrategy.add(notificationStrategy);
    }

    public synchronized void removeNotificationStrategy(NotificationStrategy notificationStrategy) {
        this.notificationStrategy.remove(notificationStrategy);
    }

    @Override
    public void update(String message) {
        for (NotificationStrategy notificationStrategy : this.notificationStrategy) {
            notificationStrategy.sendNotification(this, message);
        }
    }
}
