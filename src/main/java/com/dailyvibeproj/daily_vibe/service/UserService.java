package com.dailyvibeproj.daily_vibe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailyvibeproj.daily_vibe.model.User;
import com.dailyvibeproj.daily_vibe.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Get user by id
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}