package com.example.playquest.services;

import com.example.playquest.entities.User;
import com.example.playquest.repositories.UsersRepository;

public class UserService {
    private final UsersRepository userRepository;

    public UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    // Other methods for user-related operations
}