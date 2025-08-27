package com.dailyvibeproj.daily_vibe.service;

import org.springframework.stereotype.Service;
import com.dailyvibeproj.daily_vibe.model.*;
import com.google.genai.Client;

@Service
public class GeminiService {

    private final Client geminiClient;

    public GeminiService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String generateCustomMessage(User user) {
        String prompt = String.format(
                "You are a motivational assistant. Write a short daily affirmation for a user.And return only and only the affirmation, no other extra intro/statements required\n"
                        +
                        "User details:\n" +
                        "Name: %s\n" +
                        "Age: %s\n" +
                        "Language: %s" +
                        "Mood: %s\n" +
                        "Goals: %s\n" +
                        "Interests: %s\n" +
                        "Make it sound %s.\n",
                user.getName(), user.getAge(), user.getLanguage(), user.getMood(), user.getGoals(), user.getInterests(),
                user.getTone_preference());

        // return "you reached geminiservice";

        return geminiClient.models
                .generateContent("gemini-2.5-flash", prompt, null)
                .text();

    }
}
