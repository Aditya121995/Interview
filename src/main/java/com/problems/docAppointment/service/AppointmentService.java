package com.problems.docAppointment.service;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.Appointment;
import com.problems.docAppointment.entity.Doctor;
import com.problems.docAppointment.entity.Patient;
import com.problems.docAppointment.entity.WaitList;
import com.problems.docAppointment.enums.AppointmentStatus;
import com.problems.docAppointment.enums.Speciality;
import com.problems.docAppointment.handler.SlotRankingStrategyHandler;
import com.problems.docAppointment.handler.TimeBasedRankingHandler;
import com.problems.docAppointment.repository.AppointmentRepository;
import com.problems.docAppointment.repository.DoctorRepository;
import com.problems.docAppointment.repository.PatientRepository;
import com.problems.docAppointment.repository.WaitListRepository;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final WaitListRepository waitListRepository;
    private SlotRankingStrategyHandler rankingStrategy;

    public AppointmentService(DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              AppointmentRepository appointmentRepository,
                              WaitListRepository waitListRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.waitListRepository = waitListRepository;
        this.rankingStrategy = new TimeBasedRankingHandler();
    }

    public void setRankingStrategy(SlotRankingStrategyHandler rankingStrategy) {
        this.rankingStrategy = rankingStrategy;
    }

    public List<String> showAvailableSlotsBySpeciality(Speciality speciality) {
        List<Doctor> doctorList = doctorRepository.findDoctorBySpeciality(speciality);
        List<Map.Entry<Doctor, TimeSlot>> availableSlots = new ArrayList<>();

        for (Doctor doctor : doctorList) {
            for(TimeSlot timeSlot : doctor.getAvailableTimeSlots()) {
                boolean isSlotBooked = appointmentRepository
                        .findAppointmentByDoctorAndSlot(doctor.getDoctorId(), timeSlot).isPresent();

                if (!isSlotBooked) {
                    availableSlots.add(Map.entry(doctor, timeSlot));
                }
            }
        }

        List<Map.Entry<Doctor, TimeSlot>> rankedSlots = rankingStrategy.rankSlots(availableSlots);

        return rankedSlots.stream()
                .map(entry -> "Dr." + entry.getKey().getName() + ":(" +
                entry.getValue().toString() + ")")
                .collect(Collectors.toList());
    }

    public String bookAppointment(Doctor doctor, Patient patient, TimeSlot timeSlot) {
        try {
            Optional<Doctor> docOpt = doctorRepository.findDoctorById(doctor.getDoctorId());
            if (docOpt.isEmpty()) {
                System.out.println("Doctor not found");
                return null;
            }

            Optional<Patient> patientOpt = patientRepository.findPatientById(patient.getPatientId());
            if (patientOpt.isEmpty()) {
                System.out.println("Patient not found");
                return null;
            }

            if (hasConflictingAppointment(patient, timeSlot)) {
                System.out.println("Patient already have appointment at the same time");
                return null;
            }

            if (!doctor.isAvailableTimeSlot(timeSlot)) {
                System.out.println("Doctor is not available at this time");
                return null;
            }

            Optional<Appointment> appointmentOpt = appointmentRepository.findAppointmentByDoctorAndSlot(doctor.getDoctorId(), timeSlot);
            boolean isSlotBooked = appointmentOpt.isPresent();
            if (isSlotBooked) {
                // add to waitlist
                List<WaitList> docWaitLists = waitListRepository.findWaitListByDoctorAndSlot(doctor.getDoctorId(), timeSlot);
                boolean isSlotWaitListed = docWaitLists.stream()
                        .anyMatch(waitList -> waitList.getPatient().getPatientId().equals(patient.getPatientId()));

                if (isSlotWaitListed) {
                    System.out.println("Already added to waitlist");
                    return null;
                }

                WaitList waitListEntry = new WaitList(patient, doctor, timeSlot);
                waitListRepository.saveWaitList(waitListEntry);
                System.out.println( "Slot already booked. Added to Waitlist");
                return null;
            }

            Appointment appointment = new Appointment(patient, doctor, timeSlot, AppointmentStatus.BOOKED);
            appointmentRepository.saveAppointment(appointment);
            doctor.incrementAppointments();
            doctorRepository.saveDoctor(doctor);
            System.out.println( "Appointment booked. Booking Id :: " + appointment.getAppointmentId());
            return appointment.getAppointmentId();
        } catch (Exception e) {
            System.out.println( "Error Booking Appointment :: " + e.getMessage());
            return null;
        }
    }

    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            System.out.println("Appointment not found");
        }

        Appointment appointment = appointmentOpt.get();

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            System.out.println("Appointment is already cancelled");
        }

        appointment.cancel();
        appointmentRepository.saveAppointment(appointment);
        System.out.println("Booking Cancelled");

        // check waitlist
        Doctor doctor = appointment.getDoctor();
        TimeSlot slot = appointment.getTimeSlot();
        List<WaitList> waitLists = waitListRepository.findWaitListByDoctorAndSlotOrderByCreatedAt(doctor.getDoctorId(), slot);
        if (!waitLists.isEmpty()) {
            WaitList waitList = waitLists.get(0);
            Patient nextPatient = waitList.getPatient();
            Appointment newAppointment = new Appointment(nextPatient, doctor, slot, AppointmentStatus.BOOKED);
            appointmentRepository.saveAppointment(newAppointment);
            System.out.println( "Appointment booked for waitlisted patient :: " + nextPatient.getName()
                    + ".Booking Id :: " + newAppointment.getAppointmentId());
            // notify patient for waitlist confirmation
        } else {
            doctor.decrementAppointments();
            doctorRepository.saveDoctor(doctor);
        }


        return;
    }

    public List<String> getPatientAppointments(Patient patient) {
        List<Appointment> appointments = appointmentRepository
                .findAppointmentByPatient(patient.getPatientId());

        return appointments.stream()
                .map(entry -> "Dr." + entry.getDoctor().getName() + "-(" +
                        entry.getTimeSlot().toString() + ")" + ", ID :: " + entry.getAppointmentId())
                .collect(Collectors.toList());
    }

    public List<String> getWaitlistAppointments(Patient patient) {
        List<WaitList> waitLists = waitListRepository.findWaitListByPatient(patient.getPatientId());
        return waitLists.stream()
                .map(entry -> "Dr." + entry.getDoctor().getName() + "-(" +
                        entry.getTimeSlot().toString() + ") :: Waitlisted")
                .collect(Collectors.toList());
    }

    public List<String> getDoctorAppointments(Doctor doctor) {
        List<Appointment> appointments = appointmentRepository.findAppointmentByDoctor(doctor.getDoctorId());

        return appointments.stream()
                .map(entry -> entry.getPatient().getName() + "-(" +
                        entry.getTimeSlot().toString() + ")" +
                        ", ID :: " + entry.getAppointmentId())
                .collect(Collectors.toList());
    }

    public String getTrendingDoctor() {
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        Optional<Doctor> trendingDoctor = doctors.stream().max(Comparator.comparing(Doctor::getTotalAppointments));

        return trendingDoctor
                .map(doctor -> "Dr." + doctor.getName() + " (" + doctor.getTotalAppointments() + " appointments)")
                .orElse("No Doctors found");
    }

    private boolean hasConflictingAppointment(Patient patient, TimeSlot timeSlot) {
        List<Appointment> appointments = appointmentRepository.findAppointmentByPatient(patient.getPatientId());
        return appointments.stream()
                .anyMatch(appointment -> appointment.getTimeSlot().equals(timeSlot));

    }
}
