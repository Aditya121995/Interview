package com.problems.meetingscheduler;

public class EmailNotification implements NotificationStrategy {
    @Override
    public void sendNotification(User user, String message) {
        System.out.println("Sending email to " + user.getEmail() + " with message " + message);
    }
}
