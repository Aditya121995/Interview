package com.problems.docAppointment.repository;

import com.problems.docAppointment.entity.Patient;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PatientRepositoryImpl implements PatientRepository {

    private final Map<String, Patient> patients = new ConcurrentHashMap<>();

    @Override
    public void savePatient(Patient patient) {
        patients.put(patient.getPatientId(), patient);
    }

    @Override
    public Optional<Patient> findPatientById(String id) {
        return Optional.ofNullable(patients.get(id));
    }
}
