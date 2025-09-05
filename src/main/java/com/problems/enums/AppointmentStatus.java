package com.problems.entity;

public enum AppointmentStatus {
    BOOKED("Booked"),
    CANCELLED("Cancelled"),
    WHITELISTED("Whitelisted");

    private String value;
    AppointmentStatus(String value) {
        this.value = value;
    }
}
