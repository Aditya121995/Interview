package com.problems.concertBooking.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private final String userId;
    private final String name;
    private final String email;

    public User(String userId, String name, String email) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }
}
