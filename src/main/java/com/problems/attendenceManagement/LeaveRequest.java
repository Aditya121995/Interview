package com.problems.attendenceManagement;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class LeaveRequest {
    private final String reqId;
    private final String employeeId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LeaveType leaveType;
    private final String reason;
    private volatile LeaveStatus status;
    private final Object lock;

    public LeaveRequest(String employeeId, LocalDate startDate, LocalDate endDate,
                        LeaveType leaveType, String reason) {
        this.reqId = UUID.randomUUID().toString();
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.reason = reason;
        this.lock = new Object();
        this.status = LeaveStatus.PENDING;
    }

    public boolean approve() {
        synchronized (lock) {
            if (status == LeaveStatus.PENDING) {
                this.status = LeaveStatus.APPROVED;
                return true;
            }

            return false;
        }
    }

    public boolean reject() {
        synchronized (lock) {
            if (status == LeaveStatus.PENDING) {
                this.status = LeaveStatus.REJECTED;
                return true;
            }

            return false;
        }
    }

}
