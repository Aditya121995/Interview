package com.problems.DocAppointment.controller;

import com.problems.DocAppointment.dto.TimeSlot;
import com.problems.DocAppointment.entity.Doctor;
import com.problems.DocAppointment.entity.Patient;
import com.problems.DocAppointment.enums.Speciality;
import com.problems.DocAppointment.handler.SlotRankingStrategyHandler;
import com.problems.DocAppointment.repository.*;
import com.problems.repository.*;
import com.problems.DocAppointment.service.AppointmentService;
import com.problems.DocAppointment.service.DoctorService;
import com.problems.DocAppointment.service.PatientService;

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
