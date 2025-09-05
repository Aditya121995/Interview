package com.problems.entity;

public enum Speciality {
    CARDIOLOGIST("Cardiologist"),
    DERMATOLOGIST("Dermatologist"),
    ORTHOPEDIC("Orthopedic"),
    GENERAL_PHYSICIAN("General Physician");

    private final String name;
    Speciality(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
