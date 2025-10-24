package com.problems.meetingscheduler;

public interface NotificationStrategy {
    void sendNotification(User user, String message);
}
