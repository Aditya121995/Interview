package com.problems.repository;

import com.problems.dto.TimeSlot;
import com.problems.entity.Appointment;
import com.problems.enums.AppointmentStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final Map<String, Appointment> appointments = new ConcurrentHashMap<>();

    @Override
    public void saveAppointment(Appointment appointment) {
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    @Override
    public Optional<Appointment> findAppointmentById(String id) {
        return Optional.ofNullable(appointments.get(id));
    }

    @Override
    public List<Appointment> findAppointmentByPatient(String id) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getPatient().getPatientId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findAppointmentByDoctor(String id) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctor().getDoctorId().equals(id))
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.BOOKED)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Appointment> findAppointmentByDoctorAndSlot(String id, TimeSlot slot) {
        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctor().getDoctorId().equals(id))
                .filter(appointment -> appointment.getTimeSlot().equals(slot))
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.BOOKED)
                .findFirst();
    }
}
