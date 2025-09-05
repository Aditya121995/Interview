package com.problems.repository;

import com.problems.entity.Doctor;
import com.problems.enums.Speciality;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    void saveDoctor(Doctor doctor);
    Optional<Doctor> findDoctorById(String id);
    Optional<Doctor>  findDoctorByName(String name);
    List<Doctor> findDoctorBySpeciality(Speciality speciality);
    List<Doctor> getAllDoctors();
}
