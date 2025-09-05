package com.problems.service;

import com.problems.entity.Patient;
import com.problems.repository.PatientRepository;

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
