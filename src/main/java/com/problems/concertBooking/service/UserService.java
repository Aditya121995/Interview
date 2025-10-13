package com.problems.concertBooking.service;

import com.problems.concertBooking.entity.User;
import com.problems.concertBooking.repository.BookingRepository;
import com.problems.concertBooking.repository.UserRepository;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void addUser(User user) {
        userRepository.saveUser(user);
    }

    public User getUser(String id) {
        Optional<User> optionalUser = userRepository.getUser(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        System.out.println("User not found :: " + id);
        return null;
    }
}
