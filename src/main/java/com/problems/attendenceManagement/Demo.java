package com.problems.attendenceManagement;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        AttendanceService service = AttendanceService.getInstance();

        // Register employees
        Employee emp1 = new Employee("EMP001", "John Doe", "john@example.com", "IT");
        Employee emp2 = new Employee("EMP002", "Jane Smith", "jane@example.com", "HR");
        Employee emp3 = new Employee("EMP003", "Bob Wilson", "bob@example.com", "Sales");

        service.registerEmployee(emp1);
        service.registerEmployee(emp2);
        service.registerEmployee(emp3);

        System.out.println("=== Testing Concurrent Operations ===\n");

        // Test 1: Concurrent check-ins (only first should succeed)
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch1 = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    AttendanceRecord record = service.checkIn("EMP001");
                    System.out.println("Thread-" + threadNum + " ✓ Check-in successful: " +
                            record.getRecordId());
                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " ✗ Check-in failed: " +
                            e.getMessage());
                } finally {
                    latch1.countDown();
                }
            });
        }

        latch1.await();
        Thread.sleep(1000);

        // Test 2: Concurrent check-out attempts
        System.out.println("\n=== Testing Concurrent Check-outs ===\n");
        CountDownLatch latch2 = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    service.checkOut("EMP001");
                    System.out.println("Thread-" + threadNum + " ✓ Check-out successful");
                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " ✗ Check-out failed: " +
                            e.getMessage());
                } finally {
                    latch2.countDown();
                }
            });
        }

        latch2.await();
        Thread.sleep(1000);

        // Test 4: Leave application and approval
        System.out.println("\n=== Testing Leave Management ===\n");
        CountDownLatch latch3 = new CountDownLatch(3);

        // Multiple threads trying to apply overlapping leaves
        for (int i = 0; i < 3; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                try {
                    LeaveRequest request = service.applyLeave(
                            "EMP003",
                            LocalDate.now().plusDays(1),
                            LocalDate.now().plusDays(3),
                            LeaveType.CASUAL_LEAVE,
                            "Family function"
                    );

                    // Try to approve concurrently
                    if (request != null) {
                        System.out.println("Thread-" + threadNum + " ✓ Leave applied: " +
                                request.getReqId());
                        service.approveLeave(request.getReqId());
                        System.out.println("Thread-" + threadNum + " ✓ Leave approved: " +
                                request.getReqId());
                    }

                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " ✗ Leave operation failed: " +
                            e.getMessage());
                    e.printStackTrace();
                } finally {
                    latch3.countDown();
                }
            });
        }

        latch3.await();
        Thread.sleep(4000);

//        // Test 5: Absence marking
//        System.out.println("\n=== Testing Absence Detection ===\n");
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//        service.markAbsentEmployeesForDate(yesterday);

        // Test 6: Generate reports
        System.out.println("\n=== Attendance Statistics ===\n");
        for (Employee emp : service.getAllEmployees()) {
            System.out.println(emp);
            Map<AttendanceStatus, Long> stats = service.getAttendanceStats(
                    emp.getId(),
                    LocalDate.now().minusDays(7),
                    LocalDate.now()
            );

            System.out.println(emp.getName() + " (" + emp.getId() + "):");
            stats.forEach((status, count) ->
                    System.out.println("  " + status + ": " + count + " days")
            );
        }

        // Cleanup
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        service.shutdown();

        System.out.println("=== All tests completed ===");
    }
}
