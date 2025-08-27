package com.dailyvibeproj.daily_vibe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import com.google.genai.Client;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Bean
    public Client geminiClient() {
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            throw new IllegalStateException("Gemini API key is missing!");
        }

        return new Client.Builder()
                .apiKey(geminiApiKey)
                .build();
    }
}