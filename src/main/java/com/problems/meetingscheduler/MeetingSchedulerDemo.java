package com.problems.meetingscheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingSchedulerDemo {
    public static void main(String[] args) {
        MeetingScheduler scheduler = new MeetingScheduler();

        // Create rooms
        MeetingRoom room1 = new MeetingRoom("R1", "Conference Room A", 10);
        MeetingRoom room2 = new MeetingRoom("R2", "Conference Room B", 5);
        scheduler.addRoom(room1);
        scheduler.addRoom(room2);

        // Create users
        List<NotificationStrategy> notificationStrategies = List.of(new EmailNotification());
        User alice = new User("U1", "Alice", "alice@example.com", notificationStrategies);
        User bob = new User("U2", "Bob", "bob@example.com", notificationStrategies);
        User charlie = new User("U3", "Charlie", "charlie@example.com", notificationStrategies);

        try {
            // Book a meeting
            TimeInterval interval1 = new TimeInterval(
                    LocalDateTime.of(2025, 10, 25, 10, 0),
                    LocalDateTime.of(2025, 10, 25, 11, 0)
            );

            Meeting meeting1 = scheduler.bookMeeting(
                    "Team Standup", interval1, "R1", alice, Arrays.asList(bob, charlie)
            );

            // Check available rooms
            TimeInterval interval2 = new TimeInterval(
                    LocalDateTime.of(2025, 10, 25, 10, 30),
                    LocalDateTime.of(2025, 10, 25, 11, 30)
            );

            List<MeetingRoom> availableRooms = scheduler.getAvailableRooms(interval2);
            System.out.println("\nAvailable rooms: " + availableRooms.size());

            // Accept/Decline invitations
            scheduler.acceptInvitation(meeting1.getMeetingId(), "U2");
            scheduler.declineInvitation(meeting1.getMeetingId(), "U3");

            // Book recurring meeting
            Reccurrance recurrence = new Reccurrance(
                    RecurrenceType.WEEKLY,
                    LocalDateTime.of(2025, 11, 25, 0, 0)
            );

            TimeInterval recurringInterval = new TimeInterval(
                    LocalDateTime.of(2025, 10, 26, 14, 0),
                    LocalDateTime.of(2025, 10, 26, 15, 0)
            );

            List<Meeting> recurringMeetings = scheduler.bookRecurringMeetings(
                    "Weekly Review", recurringInterval, "R2", alice,
                    Arrays.asList(bob), recurrence
            );

            System.out.println("\nBooked " + recurringMeetings.size() +
                    " recurring meetings");

            // Modify meeting
            TimeInterval newInterval = new TimeInterval(
                    LocalDateTime.of(2025, 10, 25, 11, 0),
                    LocalDateTime.of(2025, 10, 25, 12, 0)
            );
            scheduler.modifyMeeting(meeting1.getMeetingId(), newInterval);

            // Cancel meeting
            scheduler.cancelMeeting(meeting1.getMeetingId());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

