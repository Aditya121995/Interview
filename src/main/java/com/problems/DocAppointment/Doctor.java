package com.problems.DocAppointment;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Doctor {
    private final String id;
    private final String name;
    private final Speciality speciality;
    @Setter
    private final Map<TimeSlot, DoctorSlot> availableSlots;
    private AtomicInteger appointmentCount;
    private double rating;
    private AtomicInteger ratingCount;

    public Doctor(String id, String name, Speciality speciality) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.availableSlots = new ConcurrentHashMap<>();
        this.appointmentCount = new AtomicInteger(0);
        this.rating = 0;
        this.ratingCount = new AtomicInteger(0);
    }

    public void declareAvailability(TimeSlot timeSlot) {
        if (availableSlots.containsKey(timeSlot)) {
            DoctorSlot doctorSlot = availableSlots.get(timeSlot);
            doctorSlot.reset();
        } else {
            DoctorSlot doctorSlot = new DoctorSlot(this, timeSlot);
            availableSlots.put(timeSlot, doctorSlot);
        }
    }

    public void incrementAppointmentCount() {
        this.appointmentCount.incrementAndGet();
    }

    public void decrementAppointmentCount() {
        this.appointmentCount.decrementAndGet();
    }

    public synchronized void addRating(double rating) {
        rating = (this.rating*ratingCount.get() + rating) / (ratingCount.get()+1);
        this.rating = Math.round(rating*100.0)/100.0;
        this.ratingCount.incrementAndGet();
    }

    public DoctorSlot getDoctorSlot(TimeSlot timeSlot) {
        return availableSlots.get(timeSlot);
    }

    public List<DoctorSlot> getAvailableSlots() {
        return new ArrayList<>(availableSlots.values());
    }


}
