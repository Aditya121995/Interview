package com.problems.DocAppointment;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;


@Getter
@ToString
public class Appointment {
    private final String id;
    private volatile AppointmentStatus status;
    private final Doctor doctor;
    private final Patient patient;
    private final TimeSlot timeSlot;
    private final ReentrantLock appointmentLock;

    public Appointment(Doctor doctor, Patient patient, TimeSlot timeSlot) {
        this.id = UUID.randomUUID().toString();
        this.doctor = doctor;
        this.patient = patient;
        this.timeSlot = timeSlot;
        this.appointmentLock = new ReentrantLock();
        this.status = AppointmentStatus.CONFIRMED;
    }

    public boolean tryCancel() {
        appointmentLock.lock();
        try {
            if (status == AppointmentStatus.CONFIRMED) {
                status = AppointmentStatus.CANCELLED;
                return true;
            }

            return false;
        } finally {
            appointmentLock.unlock();
        }
    }


}
