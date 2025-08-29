package com.dailyvibeproj.daily_vibe.service;

import com.dailyvibeproj.daily_vibe.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    // [id : user]
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        // optional: add one dummy user on startup
        User defaultUser = new User();
        users.put(defaultUser.getId(), defaultUser);
    }

    public User addUser(User user) {
        String newId = String.valueOf(users.size() + 1);
        user.setId(newId);
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }
}
