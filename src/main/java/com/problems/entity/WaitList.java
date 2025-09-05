package com.problems.entity;

import com.problems.dto.TimeSlot;
import com.problems.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "waitListId")
public class WaitList {
    private final String waitListId;
    private final Patient patient;
    private final Doctor doctor;
    private final TimeSlot timeSlot;
    private final Date createdAt;
    private final boolean isWaitListConfirmed;

    public WaitList(Patient patient, Doctor doctor, TimeSlot timeSlot) {
        this.waitListId = UUID.randomUUID().toString();
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.createdAt = new Date();
        this.isWaitListConfirmed = false;
    }
}
