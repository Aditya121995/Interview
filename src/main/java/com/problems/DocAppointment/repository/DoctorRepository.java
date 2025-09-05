package com.problems.DocAppointment.repository;

import com.problems.DocAppointment.entity.Doctor;
import com.problems.DocAppointment.enums.Speciality;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    void saveDoctor(Doctor doctor);
    Optional<Doctor> findDoctorById(String id);
    Optional<Doctor>  findDoctorByName(String name);
    List<Doctor> findDoctorBySpeciality(Speciality speciality);
    List<Doctor> getAllDoctors();
}
