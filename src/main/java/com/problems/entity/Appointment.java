package com.problems.entity;

import com.problems.dto.TimeSlot;
import com.problems.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "appointmentId")
public class Appointment {
    private final String appointmentId;
    private final Patient patient;
    private final Doctor doctor;
    private AppointmentStatus status;
    private final TimeSlot timeSlot;
    private final Date createdAt;
    private Date updatedAt;

    public Appointment(Patient patient, Doctor doctor, TimeSlot timeSlot, AppointmentStatus status) {
        if (patient == null || doctor == null || timeSlot == null) {
            throw new IllegalArgumentException("Patient or Doctor or TimeSlot is null");
        }

        this.appointmentId = UUID.randomUUID().toString();
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = status;
    }



    public synchronized void cancel() {
        this.status = AppointmentStatus.CANCELLED;
        this.updatedAt = new Date();
    }

    public synchronized boolean isActive() {
        return status == AppointmentStatus.BOOKED;
    }
}
