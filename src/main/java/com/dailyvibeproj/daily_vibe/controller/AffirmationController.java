package com.dailyvibeproj.daily_vibe.controller;

import com.dailyvibeproj.daily_vibe.model.User;
import com.dailyvibeproj.daily_vibe.service.GeminiService;
import com.dailyvibeproj.daily_vibe.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public String getText() {
        return geminiService.generateCustomMessage(null);
    }

    // --------------------------------- TTS ---------------------------------

    // ✅ New endpoint: Generate affirmation + TTS as MP3
    @GetMapping(value = "/tts/{id}", produces = "audio/wav")
    public ResponseEntity<byte[]> getAffirmationTTS(@PathVariable String id, HttpServletRequest request) {
        System.out.println("Incoming request======================: " + request.getRequestURI());
        User user = userService.getUser(id); // fetch from HashMap
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(("User with id " + id + " not found!").getBytes());
        }

        // 1. Generate text
        String text = geminiService.generateCustomMessage(user);

        try {
            // 2. Convert to TTS (GeminiService will handle TTS model call)
            byte[] audio = geminiService.generateTTS(text);

            // 3. Return as MP3 response
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"affirmation.wav\"")
                    .header("Cache-Control", "no-cache")
                    .header("Accept-Ranges", "bytes")
                    .body(audio);
        } catch (IOException e) {
            // If an error occurs during TTS generation, log it and send a server error
            // response.
            System.err.println("Error generating TTS audio: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favicon() {
        // This method is intentionally empty. The @ResponseStatus annotation handles
        // the response.
    }

}