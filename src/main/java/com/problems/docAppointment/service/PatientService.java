package com.problems.docAppointment.service;

import com.problems.docAppointment.entity.Patient;
import com.problems.docAppointment.repository.PatientRepository;

public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(String name) {
        Patient patient = new Patient(name);
        patientRepository.savePatient(patient);
        System.out.println("Patient registered successfully!");
        return patient;
    }
}
