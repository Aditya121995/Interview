package com.problems.meetingscheduler;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Meeting {
    private final String meetingId;
    private final User organizer;
    private String description;
    @Setter
    private TimeInterval timeInterval;
    private MeetingRoom meetingRoom;
    private MeetingStatus meetingStatus;
    private List<Invitation> invitations;
    @Setter
    private Reccurrance reccurrance;

    public Meeting(String meetingId, User organizer, String description,
                   MeetingRoom meetingRoom, TimeInterval timeInterval) {
        this.meetingId = meetingId;
        this.organizer = organizer;
        this.description = description;
        this.meetingRoom = meetingRoom;
        this.timeInterval = timeInterval;
        this.meetingStatus = MeetingStatus.SCHEDULED;
        this.invitations = new ArrayList<>();
        this.reccurrance = null;
    }

    public synchronized void addParticipant(User participant) {
        this.invitations.add(new Invitation(participant));
    }

    public synchronized void removeParticipant(User participant) {
        Invitation invitation = this.invitations.stream().filter(user -> user.equals(participant)).findFirst().orElse(null);
        if (invitation != null) {
            this.invitations.remove(invitation);
        }
    }

    public void cancel() {
        this.meetingStatus = MeetingStatus.CANCELLED;
    }
}
