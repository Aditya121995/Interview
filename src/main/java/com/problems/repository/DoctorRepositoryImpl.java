package com.problems.repository;

import com.problems.entity.Doctor;
import com.problems.enums.Speciality;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DoctorRepositoryImpl implements DoctorRepository {

    private final Map<String, Doctor> doctors = new ConcurrentHashMap<>();

    @Override
    public void saveDoctor(Doctor doctor) {
        doctors.put(doctor.getDoctorId(), doctor);
    }

    @Override
    public Optional<Doctor> findDoctorById(String id) {
        return Optional.ofNullable(doctors.get(id));
    }

    @Override
    public Optional<Doctor> findDoctorByName(String name) {
        return doctors.values().stream()
                .filter(doctor -> doctor.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<Doctor> findDoctorBySpeciality(Speciality speciality) {
        return doctors.values().stream()
                .filter(doctor -> doctor.getSpeciality().equals(speciality))
                .collect(Collectors.toList());
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctors.values().stream().toList();
    }
}
