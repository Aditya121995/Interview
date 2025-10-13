package com.problems.concertBooking.repository;

import com.problems.concertBooking.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        users = new ConcurrentHashMap<>();
    }

    public Optional<User> getUser(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public void  saveUser(User user) {
        users.put(user.getUserId(), user);
    }
}
