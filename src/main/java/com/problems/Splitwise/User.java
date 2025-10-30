package com.problems.Splitwise;

import lombok.Getter;

@Getter
public class User {
    private final String userId;
    private final String name;
    private final String email;

    public User(String id, String name,  String email) {
        this.userId = id;
        this.name = name;
        this.email = email;
    }
}
