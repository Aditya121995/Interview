package com.problems.DocAppointment;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Getter
public class Patient {
    private final String id;
    private final String name;
    private final Map<TimeSlot, Appointment> appointmentByTimeSlot;
    private final ReentrantReadWriteLock patientLock;

    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
        this.appointmentByTimeSlot = new ConcurrentHashMap<>();
        this.patientLock = new ReentrantReadWriteLock();
    }

    public boolean tryAddAppointment(Appointment appointment, TimeSlot timeSlot) {
        patientLock.writeLock().lock();
        try {
            for(Map.Entry<TimeSlot, Appointment> entry : appointmentByTimeSlot.entrySet()) {
                if (entry.getValue().getStatus().equals(AppointmentStatus.CONFIRMED)
                        && entry.getKey().overlaps(timeSlot)) {
                    return false;
                }
            }

            appointmentByTimeSlot.put(timeSlot, appointment);
            return true;
        } finally {
            patientLock.writeLock().unlock();
        }
    }

    public void removeAppointment(TimeSlot timeSlot) {
        patientLock.writeLock().lock();
        try {
            appointmentByTimeSlot.remove(timeSlot);
        }  finally {
            patientLock.writeLock().unlock();
        }
    }

    public List<Appointment> getActiveAppointments() {
        patientLock.readLock().lock();
        try {
            return appointmentByTimeSlot.values().stream()
                    .filter(app -> app.getStatus().equals(AppointmentStatus.CONFIRMED))
                    .collect(Collectors.toList());
        } finally {
            patientLock.readLock().unlock();
        }
    }
}
