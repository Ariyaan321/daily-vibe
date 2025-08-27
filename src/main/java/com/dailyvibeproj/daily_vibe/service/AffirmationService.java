package com.dailyvibeproj.daily_vibe.service;

import org.springframework.stereotype.Service;
import com.google.genai.Client;

@Service
public class AffirmationService {

    private final Client geminiClient;

    public AffirmationService(Client geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String generateAffirmation(String prompt) {
        var response = geminiClient.models.generateContent(
                "gemini-2.5-flash", prompt, null);
        return response.text();
    }
}
