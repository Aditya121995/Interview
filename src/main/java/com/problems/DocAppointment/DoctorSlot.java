package com.problems.DocAppointment;


import lombok.Getter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class DoctorSlot {
    private final Doctor doctor;
    private final TimeSlot timeSlot;
    private SlotStatus slotStatus;
    private final Queue<Patient> waitListQueue;
    private final ReentrantLock slotLock;

    public DoctorSlot(Doctor doctor, TimeSlot timeSlot) {
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        this.slotStatus = SlotStatus.AVAILABLE;
        this.waitListQueue = new ConcurrentLinkedDeque<>();
        this.slotLock = new ReentrantLock();
    }

    public void reset() {
        this.slotStatus = SlotStatus.AVAILABLE;
        this.waitListQueue.clear();
    }

    public boolean tryBook() {
        slotLock.lock();
        try {
            if (slotStatus.equals(SlotStatus.AVAILABLE)) {
                slotStatus = SlotStatus.BOOKED;
                return true;
            }
            return false;
        } finally {
            slotLock.unlock();
        }
    }

    public void release() {
        slotLock.lock();
        try {
            slotStatus = SlotStatus.AVAILABLE;
        }  finally {
            slotLock.unlock();
        }
    }

    public void addToWaitList(Patient patient) {
        waitListQueue.offer(patient);
    }

    public Patient pollFromWaitList() {
        return waitListQueue.poll();
    }
}
