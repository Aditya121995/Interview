package com.problems.DocAppointment.repository;

import com.problems.DocAppointment.entity.Patient;

import java.util.Optional;

public interface PatientRepository {
    void savePatient(Patient patient);
    Optional<Patient> findPatientById(String id);
}
