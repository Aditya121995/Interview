package com.problems.docAppointment.controller;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.Doctor;
import com.problems.docAppointment.entity.Patient;
import com.problems.docAppointment.enums.Speciality;
import com.problems.docAppointment.handler.RatingBasedRankingHandler;

import java.time.LocalTime;
import java.util.List;

public class AppointmentSystemDemo {
    public static void main(String[] args) {
        AppointmentSystemFacade system = new AppointmentSystemFacade();

        // Register patients
        Patient patient1 = system.registerPatient("Alice Johnson");
        Patient patient2 = system.registerPatient("Bob Smith");

        // Demo scenario from requirements
        System.out.println("=== Doctor Appointment Booking System Demo ===\n");

        // 1. Register Doctor
        Doctor doc1 = system.registerDoctor("Curious", Speciality.CARDIOLOGIST);

        // 2. Try invalid slot duration
        system.markDoctorAvailability("Curious",
                new TimeSlot(LocalTime.of(9,30), LocalTime.of(10,30)));

        // 3. Mark valid availability
        system.markDoctorAvailability("Curious",
                new TimeSlot(LocalTime.of(9,30), LocalTime.of(10,0)),
                new TimeSlot(LocalTime.of(12,30), LocalTime.of(13,0)),
                new TimeSlot(LocalTime.of(16,0), LocalTime.of(16,30)));

        // 4. Register another doctor
        Doctor doc2 = system.registerDoctor("Dreadful", Speciality.DERMATOLOGIST);
        system.markDoctorAvailability("Dreadful",
                new TimeSlot(LocalTime.of(9,30), LocalTime.of(10,0)),
                new TimeSlot(LocalTime.of(12,30), LocalTime.of(13,0)),
                new TimeSlot(LocalTime.of(16,0), LocalTime.of(16,30)));

        // 5. Show available slots
        List<String> slots = system.showAvailableSlotsBySpeciality(Speciality.CARDIOLOGIST);
        System.out.println("Available Cardiologist slots:");
        slots.forEach(System.out::println);

        // 6. Book appointment
        String ap1 = system.bookAppointment(doc1, patient1,
                new TimeSlot(LocalTime.of(12,30), LocalTime.of(13,0)));

        // 7. Show updated availability
        slots = system.showAvailableSlotsBySpeciality(Speciality.CARDIOLOGIST);
        System.out.println("Updated Cardiologist slots:");
        slots.forEach(System.out::println);

        // 8. Cancel booking
        system.cancelAppointment(ap1);

        // 9. Show availability after cancellation
        slots = system.showAvailableSlotsBySpeciality(Speciality.CARDIOLOGIST);
        System.out.println("Available Cardiologist slots:");
        slots.forEach(System.out::println);

        // 10. Book again
        String ap2 = system.bookAppointment(doc1, patient2,
                new TimeSlot(LocalTime.of(12,30), LocalTime.of(13,0)));

        // 11. Register and mark availability for another doctor
        Doctor doc3 = system.registerDoctor("Daring", Speciality.DERMATOLOGIST);
        system.markDoctorAvailability("Daring",
                new TimeSlot(LocalTime.of(11,30), LocalTime.of(12,0)),
                new TimeSlot(LocalTime.of(14,0), LocalTime.of(14,30)));

        // 12. Show dermatologist slots (ranked by time)
        slots = system.showAvailableSlotsBySpeciality(Speciality.DERMATOLOGIST);
        System.out.println("Available DERMATOLOGIST slots:");
        slots.forEach(System.out::println);

        // 13. Demo waitlist functionality
        String ap3 = system.bookAppointment(doc2, patient1,
                new TimeSlot(LocalTime.of(9,30), LocalTime.of(10,0)));

        String ap4 = system.bookAppointment(doc2, patient2,
                new TimeSlot(LocalTime.of(9,30), LocalTime.of(10,0))); // wailisted

        system.cancelAppointment(ap3); // patient 2 get the appointment

        // 14. Show trending doctor
        System.out.println("Trending doctor: " + system.getTrendingDoctor());

        // 15. Demo extensibility - change ranking strategy
        system.setRankingStrategy(new RatingBasedRankingHandler());
        doc3.setRating(4.0);
        doc2.setRating(2.0);
        slots = system.showAvailableSlotsBySpeciality(Speciality.DERMATOLOGIST);
        System.out.println("Available DERMATOLOGIST slots:");
        slots.forEach(System.out::println);

        // 16. View patient appointments
        System.out.println("Patien 1 Appointments :");
        List<String> pa1 = system.getPatientAppointments(patient1);
        pa1.forEach(System.out::println);
        System.out.println("Patien 2 Appointments :");
        List<String> pa2 = system.getPatientAppointments(patient2);
        pa2.forEach(System.out::println);
    }
}
