package com.problems.repository;

import com.problems.entity.Patient;

import java.util.Optional;

public interface PatientRepository {
    void savePatient(Patient patient);
    Optional<Patient> findPatientById(String id);
}
