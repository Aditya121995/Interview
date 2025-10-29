package com.problems.attendenceManagement;

import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Employee {
    private final String id;
    private final String name;
    private final String email;
    private final String department;
    private final ReentrantLock lock;

    public Employee(String id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.lock = new ReentrantLock();
    }
}
