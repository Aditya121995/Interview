package com.problems.docAppointment.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "patientId")
public class Patient{
    protected final String patientId;
    protected final String name;

    public Patient(String name) {
        this.patientId= UUID.randomUUID().toString();
        this.name=name;
    }
}
