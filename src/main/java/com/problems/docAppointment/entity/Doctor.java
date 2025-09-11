package com.problems.docAppointment.entity;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.enums.Speciality;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "doctorId")
public class Doctor {
    private final String doctorId;
    private final String name;
    private final Speciality speciality;
    private final Set<TimeSlot> availableTimeSlots;
    private Double rating;
    private Integer totalAppointments;

    public Doctor(String name, Speciality speciality) {
        if (speciality == null || name == null) {
            throw new IllegalArgumentException("speciality and name cannot be null");
        }
        this.doctorId = UUID.randomUUID().toString();
        this.name = name;
        this.speciality = speciality;
        this.availableTimeSlots = new HashSet<>();
        this.rating = 0.0;
        this.totalAppointments = 0;
    }

    public synchronized void addAvailableTimeSlot(TimeSlot timeSlots) {
        this.availableTimeSlots.add(timeSlots);
    }

    public synchronized void removeAvailableTimeSlot(TimeSlot timeSlot) {
        availableTimeSlots.remove(timeSlot);
    }

    public synchronized boolean isAvailableTimeSlot(TimeSlot timeSlot) {
        return availableTimeSlots.contains(timeSlot);
    }

    public synchronized void incrementAppointments() {
        totalAppointments++;
    }

    public synchronized void decrementAppointments() {
        if (totalAppointments > 0) {
            totalAppointments--;
        }
    }

    public boolean hasConflictingSlot(TimeSlot ts) {
        return availableTimeSlots.stream().anyMatch(t -> t.overlaps(ts));
    }
}
