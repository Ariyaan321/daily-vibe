package com.dailyvibeproj.daily_vibe.controller;

import com.dailyvibeproj.daily_vibe.model.User;
import com.dailyvibeproj.daily_vibe.service.GeminiService;
import com.dailyvibeproj.daily_vibe.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/affirmations")
public class AffirmationController {

    private final GeminiService geminiService;
    private final UserService userService;

    public AffirmationController(GeminiService geminiService, UserService userService) {
        this.geminiService = geminiService;
        this.userService = userService;

    }

    @GetMapping("/{id}")
    public String getAffirmation(@PathVariable String id) {
        User user = userService.getUser(id); // fetch from HashMap
        if (user == null) {
            return "User with id " + id + " not found!";
        }
        return geminiService.generateCustomMessage(user);
    }

    // @PostMapping
    // public String getAffirmation(@RequestBody User user) {
    // // return "you reached here";
    // return geminiService.generateCustomMessage(user);
    // }
}