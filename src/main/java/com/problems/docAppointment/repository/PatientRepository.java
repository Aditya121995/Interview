package com.problems.docAppointment.repository;

import com.problems.docAppointment.entity.Patient;

import java.util.Optional;

public interface PatientRepository {
    void savePatient(Patient patient);
    Optional<Patient> findPatientById(String id);
}
