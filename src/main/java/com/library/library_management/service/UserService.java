package com.library.library_management.service;

import com.library.library_management.entity.User;
import com.library.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "User already exists";
        }
        userRepository.save(user);
        return "User created successfully";
    }

    public String login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null) {
            return "User not found";
        }
        if (!existingUser.getPassword().equals(user.getPassword())) {
            return "Invalid password";
        }
        return "Login successful";
    }
}
