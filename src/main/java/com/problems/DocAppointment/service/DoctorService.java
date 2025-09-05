package com.problems.DocAppointment.service;

import com.problems.DocAppointment.dto.TimeSlot;
import com.problems.DocAppointment.entity.Doctor;
import com.problems.DocAppointment.enums.Speciality;
import com.problems.DocAppointment.repository.DoctorRepository;
import com.problems.DocAppointment.validator.TimeSlotValidator;

import java.util.Optional;

public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    public Doctor registerDoctor(String name, Speciality speciality) {
        Doctor doctor = new Doctor(name, speciality);
        doctorRepository.saveDoctor(doctor);
        System.out.println("Welcome Dr." + name + " !!");
        return doctor;
    }

    public void setAvailableSlot(String name, TimeSlot... timeSlots) {
        Optional<Doctor> doctorOpt = doctorRepository.findDoctorByName(name);
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor Not Found");
            return;
        }

        Doctor doctor = doctorOpt.get();
        try {
            for (TimeSlot ts : timeSlots) {
                TimeSlotValidator.validateTimeSlot(ts.getStartTime(), ts.getEndTime());
                doctor.addAvailableTimeSlot(ts);
            }

            doctorRepository.saveDoctor(doctor);
            System.out.println("Done Doc!");
        } catch (Exception e) {
            System.out.println("Error :: " + e.getMessage());
        }
    }
}
