package com.problems.docAppointment.enums;

public enum AppointmentStatus {
    BOOKED("Booked"),
    CANCELLED("Cancelled");

    private final String name;
    AppointmentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
