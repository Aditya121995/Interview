package com.problems.docAppointment.repository;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    void saveAppointment(Appointment appointment);
    Optional<Appointment> findAppointmentById(String id);
    List<Appointment> findAppointmentByPatient(String id);
    List<Appointment> findAppointmentByDoctor(String id);
    Optional<Appointment> findAppointmentByDoctorAndSlot(String id, TimeSlot slot);
}
