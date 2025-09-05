package com.problems.repository;

import com.problems.dto.TimeSlot;
import com.problems.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    void saveAppointment(Appointment appointment);
    Optional<Appointment> findAppointmentById(String id);
    List<Appointment> findAppointmentByPatient(String id);
    List<Appointment> findAppointmentByDoctor(String id);
    Optional<Appointment> findAppointmentByDoctorAndSlot(String id, TimeSlot slot);
}
