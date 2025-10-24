package com.problems.meetingscheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MeetingScheduler {
    private final Map<String, MeetingRoom> rooms;
    private final Map<String, Meeting> meetings;
    private final ReentrantLock lock;

    public MeetingScheduler() {
        this.rooms = new ConcurrentHashMap<>();
        this.meetings = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
    }

    public void addRoom(MeetingRoom room) {
        rooms.put(room.getRoomId(), room);
    }

    public Meeting bookMeeting(String title, TimeInterval interval, String roomId, User organizer,
                               List<User> participants) {

        lock.lock();
        try {
            MeetingRoom room = this.rooms.get(roomId);
            if (room == null) {
                System.out.println("Room not found");
                return null;
            }

            if (!room.isAvailable(interval)) {
                System.out.println("Room is not available for interval " + interval);
                return null;
            }

            String meetingId = UUID.randomUUID().toString();
            Meeting meeting = new Meeting(meetingId, organizer, title, room, interval);

            for (User participant : participants) {
                meeting.addParticipant(participant);
            }

            room.addMeeting(meeting);
            this.meetings.put(meetingId, meeting);

            for (User participant : participants) {
                participant.update("Meeting '" + title + "' has been scheduled from interval : " +
                        interval.getStartTime() + " to " + interval.getEndTime());
            }

            return meeting;
        } finally {
            lock.unlock();
        }
    }

    public List<Meeting> bookRecurringMeetings(String title, TimeInterval interval, String roomId, User organizer,
                                               List<User> participants, Reccurrance reccurrance) {

        List<Meeting> recurringMeets = new ArrayList<>();
        List<TimeInterval> recurringIntervals = reccurrance.generateOccurrences(interval);

        for (TimeInterval recurringInterval : recurringIntervals) {
            Meeting meeting = bookMeeting(title, recurringInterval, roomId, organizer, participants);
            meeting.setReccurrance(reccurrance);
            recurringMeets.add(meeting);
        }

        return recurringMeets;
    }

    public List<MeetingRoom> getAvailableRooms(TimeInterval interval) {
        List<MeetingRoom> availableRooms = new ArrayList<>();
        for (MeetingRoom room : this.rooms.values()) {
            if (room.isAvailable(interval)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public void cancelMeeting(String meetingId) {
        Meeting meeting = this.meetings.get(meetingId);
        if (meeting == null) {
            System.out.println("Meeting not found");
            return;
        }

        lock.lock();
        try {
            meeting.cancel();
            meeting.getMeetingRoom().removeMeeting(meeting);

            for (Invitation invitation : meeting.getInvitations()) {
                User user = invitation.getUser();
                user.update("Meeting :: " + meeting.getDescription() + " has been cancelled");
            }
        } finally {
            lock.unlock();
        }
    }

    public void acceptInvitation(String meetingId, String userId) {
        Meeting meeting = this.meetings.get(meetingId);
        if (meeting == null) {
            System.out.println("Meeting not found");
            return;
        }

        for (Invitation invitation : meeting.getInvitations()) {
            if (invitation.getUser().getUserId().equals(userId)) {
                invitation.acceptInvitation();
                System.out.println("Invitation accepted by user : " + invitation.getUser().getName());
                return;
            }
        }

        System.out.println("Invitation Not Found for user : " + userId);
    }

    public void declineInvitation(String meetingId, String userId) {
        Meeting meeting = this.meetings.get(meetingId);
        if (meeting == null) {
            System.out.println("Meeting not found");
            return;
        }

        for (Invitation invitation : meeting.getInvitations()) {
            if (invitation.getUser().getUserId().equals(userId)) {
                invitation.declineInvitation();
                System.out.println("Invitation declined by user : " + invitation.getUser().getName());
                return;
            }
        }

        System.out.println("Invitation Not Found for user : " + userId);
    }

    public void modifyMeeting(String meetingId, TimeInterval timeInterval) {
        Meeting meeting = this.meetings.get(meetingId);
        if (meeting == null) {
            System.out.println("Meeting not found");
            return;
        }

        lock.lock();
        try {
            MeetingRoom room = meeting.getMeetingRoom();

            room.removeMeeting(meeting); // temp remove

            if (!room.isAvailable(timeInterval)) {
                room.addMeeting(meeting);
                System.out.println("Room is not available for interval " + timeInterval);
                return;
            }

            meeting.setTimeInterval(timeInterval);

            for (Invitation invitation : meeting.getInvitations()) {
                User user = invitation.getUser();
                user.update("Meeting :: " + meeting.getDescription() + " has been rescheduled to interval : " +
                        timeInterval.getStartTime()  + " to " + timeInterval.getEndTime());
            }


        } finally {
            lock.unlock();
        }
    }
}
