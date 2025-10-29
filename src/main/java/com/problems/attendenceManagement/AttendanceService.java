package com.problems.attendenceManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AttendanceService {
    private static AttendanceService instance;
    private final Map<String, AttendanceRecord> attendanceRecords;
    private final Map<String, LeaveRequest> leaveRequests;
    private final Map<String, Employee> employees;
    private final ScheduledExecutorService scheduler;

    private AttendanceService() {
        this.attendanceRecords = new ConcurrentHashMap<>();
        this.leaveRequests = new ConcurrentHashMap<>();
        this.employees = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduleDailyAbsentMarking();
    }

    public static synchronized AttendanceService getInstance() {
        if (instance == null) {
            instance = new AttendanceService();
        }
        return instance;
    }

    private void scheduleDailyAbsentMarking() {
        // run daily at 11:59 pm
        LocalTime targetTime = LocalTime.of(23, 59);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.with(targetTime);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        long initialDelay = ChronoUnit.MINUTES.between(now, nextRun);

        scheduler.scheduleAtFixedRate(this::markAbsentEmployees, 5, 5, TimeUnit.SECONDS);

    }

    private void markAbsentEmployees() {
        LocalDate date = LocalDate.now().minusDays(1);
        for (Employee employee : employees.values()) {
            ReentrantLock lock = employee.getLock();
            lock.lock();
            try {
                String key = generateKey("ATT", employee.getId(), date);
                attendanceRecords.computeIfAbsent(key, k->{
                    return new AttendanceRecord(key, employee.getId(), date);
                });
            } finally {
                lock.unlock();
            }
        }
    }

    public void registerEmployee(Employee emp) {
        this.employees.put(emp.getId(), emp);
    }

    public AttendanceRecord checkIn(String employeeId) {
        if (!employees.containsKey(employeeId)) {
            System.err.println("Employee " + employeeId + " does not exist");
            return null;
        }

        Employee employee = employees.get(employeeId);
        ReentrantLock lock = employee.getLock();
        lock.lock();
        try {
            LocalDate today =  LocalDate.now();
            String key = generateKey("ATT", employeeId, today);

            AttendanceRecord record = attendanceRecords.computeIfAbsent(key, k->{
                return new AttendanceRecord(key, employeeId, today);
            });

            record.markCheckIn(LocalDateTime.now());
            return record;
        } finally {
            lock.unlock();
        }
    }

    public void checkOut(String employeeId) {
        if (!employees.containsKey(employeeId)) {
            System.err.println("Employee " + employeeId + " does not exist");
            return;
        }

        Employee employee = employees.get(employeeId);
        ReentrantLock lock = employee.getLock();
        lock.lock();
        try {
            LocalDate today =  LocalDate.now();
            String key = generateKey("ATT", employeeId, today);
            AttendanceRecord record = attendanceRecords.get(key);
            if (record == null) {
                System.out.println("Attendance record does not exist");
                return;
            }
            record.markCheckOut(LocalDateTime.now());
        } finally {
            lock.unlock();
        }
    }

    public LeaveRequest applyLeave(String employeeId, LocalDate startDate, LocalDate endDate,
                                   LeaveType leaveType, String reason) {
        if (!employees.containsKey(employeeId)) {
            System.err.println("Employee " + employeeId + " does not exist");
            return null;
        }

        if (startDate.isAfter(endDate)) {
            System.err.println("Start date cannot be after end date");
            return null;
        }

        Employee employee = employees.get(employeeId);
        ReentrantLock lock = employee.getLock();
        lock.lock();
        try {
            // check for overlapping leaves
            boolean hasOverlap = leaveRequests.values().stream()
                    .filter(lr -> lr.getEmployeeId().equals(employeeId))
                    .filter(lr -> lr.getStatus().equals(LeaveStatus.APPROVED) || lr.getStatus().equals(LeaveStatus.PENDING))
                    .anyMatch(lr -> !lr.getStartDate().isAfter(endDate) && !startDate.isAfter(lr.getEndDate()));

            if (hasOverlap) {
                System.out.println("Overlapping approved Leave request has already been executed");
                return null;
            }

            LeaveRequest request = new LeaveRequest(employeeId, startDate, endDate, leaveType, reason);
            leaveRequests.put(request.getReqId(), request);
            return request;
        } finally {
            lock.unlock();
        }
    }

    public void approveLeave(String requestId) {
        if (!leaveRequests.containsKey(requestId)) {
            System.err.println("Request " + requestId + " does not exist");
            return;
        }
        LeaveRequest request = leaveRequests.get(requestId);

        String employeeId = request.getEmployeeId();
        Employee employee = employees.get(employeeId);
        ReentrantLock lock = employee.getLock();
        lock.lock();

        try {
            boolean isApproved = request.approve();
            if (!isApproved) {
                System.out.println("Approving Leave request has been failed");
                return;
            }

            LocalDate currentDate = request.getStartDate();
            while (!currentDate.isAfter(request.getEndDate())) {
                final LocalDate date = currentDate;
                String key = generateKey("ATT", employeeId, currentDate);
                AttendanceRecord record = attendanceRecords.computeIfAbsent(key, k->{
                    return new AttendanceRecord(key, employeeId, date);
                });
                record.markOnLeave();

                currentDate = currentDate.plusDays(1);
            }
        } finally {
            lock.unlock();
        }
    }

    public void rejectLeave(String requestId) {
        if (!leaveRequests.containsKey(requestId)) {
            System.err.println("Request " + requestId + " does not exist");
            return;
        }
        LeaveRequest request = leaveRequests.get(requestId);
        boolean rejected = request.reject();
        if (!rejected) {
            System.out.println("Rejected Leave request has been failed");
        }
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public List<AttendanceRecord> getAllAttendanceRecordsByEmployeeId(String employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        LocalDate currentDate =  startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate date = currentDate;
            String key = generateKey("ATT", employeeId, currentDate);
            AttendanceRecord record = attendanceRecords.get(key);
            if (record != null) {
                records.add(record);
            }
            currentDate = currentDate.plusDays(1);
        }

        return records;
    }

    public Map<AttendanceStatus, Long> getAttendanceStats(String employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = getAllAttendanceRecordsByEmployeeId(employeeId, startDate, endDate);
        return records.stream().collect(Collectors.groupingBy(AttendanceRecord::getStatus, Collectors.counting()));
    }

    private String generateKey(String s, String employeeId, LocalDate today) {
        return s + "-" + employeeId + "-" + today.toString();
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if(scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
