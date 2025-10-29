package com.problems.docAppointment;

import javax.print.Doc;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AppointmentBookingSystem {
    private static AppointmentBookingSystem instance;
    private final Map<String, Patient> patients;
    private final Map<String, Doctor> doctors;
    private final Map<String, Appointment> appointments;
    private SlotRankingStrategy slotRankingStrategy;

    private AppointmentBookingSystem() {
        patients = new ConcurrentHashMap<>();
        doctors = new ConcurrentHashMap<>();
        appointments = new ConcurrentHashMap<>();
        slotRankingStrategy = new TimeBasedRankingStrategy();
    }

    public static synchronized AppointmentBookingSystem getInstance() {
        if (instance == null) {
            instance = new AppointmentBookingSystem();
        }
        return instance;
    }

    public synchronized void setSlotRankingStrategy(SlotRankingStrategy slotRankingStrategy) {
        this.slotRankingStrategy = slotRankingStrategy;
    }

    public void registerPatient(Patient patient) {
        if (patients.containsKey(patient.getId())) {
            System.out.println(patient.getId() + " is already registered");
            return;
        }
        patients.put(patient.getId(), patient);
    }

    public void registerDoctor(Doctor doctor) {
        if (doctors.containsKey(doctor.getId())) {
            System.out.println(doctor.getId() + " is already registered");
            return;
        }
        doctors.put(doctor.getId(), doctor);
    }

    public void declareAvailability(String doctorId, List<TimeSlot> slots) {
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            System.out.println(doctorId + " does not exist");
            return;
        }

        for (TimeSlot slot : slots) {
            doctor.declareAvailability(slot);
        }
    }

    public List<DoctorSlot> searchAvailableSlots(Speciality speciality) {
        List<DoctorSlot> availableSlots = doctors.values().stream()
                .filter(doctor -> doctor.getSpeciality().equals(speciality))
                .flatMap(doctor -> doctor.getAvailableSlots().stream())
                .toList();

        return slotRankingStrategy.rank(availableSlots);
    }

    public void rankDoctor(String doctorId, double rating) {
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            System.out.println(doctorId + " does not exist");
            return;
        }

        doctor.addRating(rating);
    }

    public Appointment bookAppointment(String patientId, String doctorId, TimeSlot slot) {
        System.out.println(patientId + " booking " + doctorId);
        Patient patient = patients.get(patientId);
        Doctor doctor = doctors.get(doctorId);
        if (patient == null) {
            System.out.println(patientId + " does not exist");
            return null;
        }
        if (doctor == null) {
            System.out.println(doctorId + " does not exist");
            return null;
        }

        DoctorSlot doctorSlot = doctor.getDoctorSlot(slot);
        if (doctorSlot == null) {
            System.out.println("slot is not declared by doctor");
            return null;
        }

        doctorSlot.getSlotLock().lock();
        try {
            if (doctorSlot.getSlotStatus() != SlotStatus.AVAILABLE) {
                doctorSlot.addToWaitList(patient);
                System.out.println("slot is already booked. Added to waitlist for now");
                return null;
            }

            Appointment appointment = new Appointment(doctor, patient, slot);

            if(!patient.tryAddAppointment(appointment, slot)) {
                System.out.println("appointment failed. Patient " + patient.getName() + " has conflicting appointment");
                return null;
            }

            doctorSlot.tryBook(); // this will always pass since I have a lock acquired on the doctor slot

            appointments.put(appointment.getId(), appointment);
            doctor.incrementAppointmentCount();
            return appointment;
        } finally {
            doctorSlot.getSlotLock().unlock();
        }

    }

    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            System.out.println("appointment does not exist");
            return;
        }

        if(!appointment.tryCancel()) {
            System.out.println("appointment already cancelled");
            return;
        }

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        TimeSlot timeSlot = appointment.getTimeSlot();
        DoctorSlot doctorSlot = doctor.getDoctorSlot(timeSlot);

        doctorSlot.getSlotLock().lock();
        try {
            doctorSlot.release();
            Patient waitlistPatient = null;
            while ((waitlistPatient = doctorSlot.pollFromWaitList()) != null) {
                Appointment newAppointment = new Appointment(doctor, patient, timeSlot);
                if(!patient.tryAddAppointment(appointment, timeSlot)) {
                    System.out.println("appointment failed. Patient " + patient.getName() + " has conflicting appointment");
                    continue;
                }

                doctorSlot.tryBook();
                appointments.put(appointment.getId(), appointment);
                doctor.incrementAppointmentCount();
                // notify the patient for waitlist confirmation
                return;
            }
        } finally {
            doctorSlot.getSlotLock().unlock();
        }
    }

    public List<Appointment> getPatientAppointments(String patientId) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            System.out.println(patientId + " does not exist");
            return null;
        }

        return patient.getActiveAppointments();
    }

    public List<Appointment> getDoctorAppointments(String doctorId) {
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            System.out.println(doctorId + " does not exist");
            return null;
        }

        return appointments.values().stream()
                .filter(appointment -> appointment.getDoctor().getId().equals(doctorId))
                .filter(appointment -> appointment.getStatus().equals(AppointmentStatus.CONFIRMED))
                .sorted(Comparator.comparing(appointment -> appointment.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }

    public Doctor getTrendingDoctor() {
        return doctors.values().stream()
                .max(Comparator.comparing(doctor -> doctor.getAppointmentCount().get()))
                .orElse(null);
    }


}
