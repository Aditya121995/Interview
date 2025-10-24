package com.problems.meetingscheduler;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Invitation {
    private final String invitationId;
    private InvitationStatus invitationStatus;
    private final User user;

    public Invitation(User user) {
        this.invitationId = UUID.randomUUID().toString();
        this.user = user;
        this.invitationStatus = InvitationStatus.PENDING;
    }

    public void acceptInvitation() {
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }

    public void declineInvitation() {
        this.invitationStatus = InvitationStatus.DECLINED;
    }
}
