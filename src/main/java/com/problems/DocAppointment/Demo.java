package com.problems.DocAppointment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        AppointmentBookingSystem system = AppointmentBookingSystem.getInstance();

        System.out.println("========================================");
        System.out.println("DOCTOR APPOINTMENT BOOKING SYSTEM DEMO");
        System.out.println("========================================\n");

        system.registerDoctor(new Doctor("doc1", "Dr. Smith", Speciality.CARDIOLOGIST));
        system.registerDoctor(new Doctor("doc2", "Dr. Johnson", Speciality.DERMATOLOGIST));
        system.registerDoctor(new Doctor("doc3", "Dr. Williams", Speciality.CARDIOLOGIST));
        system.registerDoctor(new Doctor("doc4", "Dr. Brown", Speciality.ORTHOPEDIC));
        system.registerDoctor(new Doctor("doc5", "Dr. Taylor", Speciality.GENERAL_PHYSICIAN));

        system.registerPatient(new Patient("p1", "Alice"));
        system.registerPatient(new Patient("p2", "Bob"));

        List<TimeSlot> slots = new ArrayList<>(List.of(
                new TimeSlot(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 0)),
                new TimeSlot(LocalTime.of(10, 0), LocalTime.of(10, 30))
        ));

        system.declareAvailability("doc3", new ArrayList<>(slots));
        system.declareAvailability("doc1", new ArrayList<>(slots));
        system.declareAvailability("doc2", new ArrayList<>(slots));
        TimeSlot ts1 = new TimeSlot(LocalTime.of(11, 15), LocalTime.of(11, 45));
        slots.add(ts1);
        system.declareAvailability("doc4", new ArrayList<>(slots));
        slots.add(new TimeSlot(LocalTime.of(11, 0), LocalTime.of(11, 30)));
        system.declareAvailability("doc5", new ArrayList<>(slots));

        List<DoctorSlot> availableSlots = system.searchAvailableSlots(Speciality.CARDIOLOGIST);

        System.out.println("✓ Found " + availableSlots.size() + " available slots for CARDIOLOGIST");
        for (DoctorSlot slot : availableSlots) {
            System.out.println("  - Dr. " + slot.getDoctor().getName() +
                    " | " + slot.getTimeSlot().getStartTime() +
                    "-" + slot.getTimeSlot().getEndTime());
        }

        Appointment appointment1 = system.bookAppointment("p1", "doc5",
                new TimeSlot(LocalTime.of(11, 0), LocalTime.of(11, 30)));
        System.out.println("Appointment booked :: " + appointment1);

        Appointment appointment2 = system.bookAppointment("p1", "doc4",
                new TimeSlot(LocalTime.of(11, 15), LocalTime.of(11, 45)));
        System.out.println("Appointment failed :: " + appointment2);


        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            system.registerPatient(new Patient("p" + i, "Patient " + i));
        }

        for (int i = 0; i < 10; i++) {
            String patientId = "p" + i;
            executor.submit(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                system.bookAppointment(patientId, "doc4",  new TimeSlot(LocalTime.of(11, 15), LocalTime.of(11, 45)));
            });
        }

        executor.shutdown();





    }
}
