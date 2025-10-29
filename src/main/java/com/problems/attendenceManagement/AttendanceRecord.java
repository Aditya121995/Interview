package com.problems.attendenceManagement;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AttendanceRecord {
    private final String recordId;
    private final String employeeId;
    private final LocalDate date;
    private volatile AttendanceStatus status;
    private volatile LocalDateTime checkInTime;
    private volatile LocalDateTime checkOutTime;
    private final Object lock;

    public AttendanceRecord(String recordId, String employeeId, LocalDate date) {
        this.recordId = recordId;
        this.employeeId = employeeId;
        this.date = date;
        this.lock = new Object();
        this.status = AttendanceStatus.ABSENT;
    }

    public void markCheckIn(LocalDateTime checkInTime) {
        synchronized (lock) {
            if (this.checkInTime != null) {
                System.out.println("Already checked in");
                return;
            }

            this.checkInTime = checkInTime;
            this.status = AttendanceStatus.PRESENT;
        }
    }

    public void markCheckOut(LocalDateTime checkOutTime) {
        synchronized (lock) {
            if (this.checkInTime == null) {
                System.out.println("Checking needs to be done for checkout");
                return;
            }

            if (this.checkOutTime != null) {
                System.out.println("Already checked out");
                return;
            }

            this.checkOutTime = checkOutTime;
        }
    }

    public void markOnLeave() {
        synchronized (lock) {
            if (this.status == AttendanceStatus.PRESENT) {
                System.out.println("Cannot mark leave for the PRESENT status");
                return;
            }
        }
    }

}
