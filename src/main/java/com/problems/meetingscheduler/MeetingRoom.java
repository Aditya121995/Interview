package com.problems.meetingscheduler;

import lombok.Getter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MeetingRoom {
    private final String roomId;
    private final String roomName;
    private final int capacity;
    private final List<Meeting> meetings;

    public MeetingRoom(String roomId, String roomName, int capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.meetings = new ArrayList<>();
    }

    public synchronized void addMeeting(Meeting meeting) {
        this.meetings.add(meeting);
    }

    public synchronized void removeMeeting(Meeting meeting) {
        this.meetings.remove(meeting);
    }

    public synchronized boolean isAvailable(TimeInterval interval) {
        for (Meeting meeting : meetings) {
            if (meeting.getMeetingStatus().equals(MeetingStatus.SCHEDULED) && meeting.getTimeInterval().isOverlap(interval)) {
                return false;
            }
        }
        return true;
    }
}
