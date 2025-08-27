package com.dailyvibeproj.daily_vibe.controller;

import com.dailyvibeproj.daily_vibe.service.AffirmationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/affirmations")
public class AffirmationController {

    private final AffirmationService affirmationService;

    public AffirmationController(AffirmationService affirmationService) {
        this.affirmationService = affirmationService;
    }

    // Simple test endpoint
    @GetMapping("/generate")
    public String generateAffirmation(@RequestParam(defaultValue = "Give me a positive affirmation") String prompt) {
        return affirmationService.generateAffirmation(prompt);
    }
}