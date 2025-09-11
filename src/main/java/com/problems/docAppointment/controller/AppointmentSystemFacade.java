package com.problems.docAppointment.controller;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.Doctor;
import com.problems.docAppointment.entity.Patient;
import com.problems.docAppointment.enums.Speciality;
import com.problems.docAppointment.handler.SlotRankingStrategyHandler;
import com.problems.docAppointment.repository.*;
import com.problems.docAppointment.service.AppointmentService;
import com.problems.docAppointment.service.DoctorService;
import com.problems.docAppointment.service.PatientService;

import java.util.List;

public class AppointmentSystemFacade {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentSystemFacade() {
        DoctorRepository doctorRepository = new DoctorRepositoryImpl();
        PatientRepository patientRepository = new PatientRepositoryImpl();
        AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl();
        WaitListRepository waitListRepository = new WaitListRepositoryImpl();

        appointmentService = new AppointmentService(doctorRepository, patientRepository, appointmentRepository, waitListRepository);
        doctorService = new DoctorService(doctorRepository);
        patientService = new PatientService(patientRepository);
    }

    public Doctor registerDoctor(String name, Speciality speciality) {
        return doctorService.registerDoctor(name, speciality);
    }

    public void markDoctorAvailability(String name, TimeSlot... timeSlots) {
        doctorService.setAvailableSlot(name, timeSlots);
    }

    public Patient registerPatient(String name) {
        return patientService.registerPatient(name);
    }

    public List<String> showAvailableSlotsBySpeciality(Speciality speciality) {
        return appointmentService.showAvailableSlotsBySpeciality(speciality);
    }

    public String bookAppointment(Doctor doctor, Patient patient, TimeSlot timeSlot) {
        return appointmentService.bookAppointment(doctor, patient, timeSlot);
    }

    public void cancelAppointment(String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
    }

    public List<String> getPatientAppointments(Patient patient) {
        return appointmentService.getPatientAppointments(patient);
    }

    public List<String> getWaitlistAppointments(Patient patient) {
        return appointmentService.getWaitlistAppointments(patient);
    }

    public List<String> getDoctorAppointments(Doctor doctor) {
        return appointmentService.getDoctorAppointments(doctor);
    }

    public String getTrendingDoctor() {
        return appointmentService.getTrendingDoctor();
    }

    public void setRankingStrategy(SlotRankingStrategyHandler rankingStrategy) {
        appointmentService.setRankingStrategy(rankingStrategy);
    }

}
