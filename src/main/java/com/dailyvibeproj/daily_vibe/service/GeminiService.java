package com.dailyvibeproj.daily_vibe.service;

// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.util.List;
// import java.util.Base64;
// import java.io.File;

// import org.springframework.stereotype.Service;
import com.dailyvibeproj.daily_vibe.model.*;
import com.google.genai.Client;
// import com.google.genai.types.GenerateContentConfig;
// import com.google.genai.types.GenerateContentResponse;
// import com.google.genai.types.PrebuiltVoiceConfig;
// import com.google.genai.types.SpeechConfig;
// import com.google.genai.types.VoiceConfig;
// import com.google.genai.types.Content;
// import com.google.genai.types.Part;

import java.util.List;

import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.SpeechConfig;
import com.google.genai.types.VoiceConfig;
import com.google.genai.types.PrebuiltVoiceConfig;

import org.springframework.stereotype.Service;

@Service
public class GeminiService {

        private final Client geminiClient;

        public GeminiService(Client geminiClient) {
                this.geminiClient = geminiClient;
        }

        public String generateCustomMessage(User user) {
                if (user == null) {
                        String prompt = "You are a motivational assistant. Write a short affirmation for a user.And return only and only the affirmation, no other extra intro/statements required";
                        return geminiClient.models
                                        .generateContent("gemini-2.5-flash", prompt, null)
                                        .text();

                } else {
                        String prompt = String.format(
                                        "You are a motivational assistant. Write a short affirmation for a user.And return only and only the affirmation, no other extra intro/statements required\n"
                                                        +
                                                        "User details:\n" +
                                                        "Name: %s\n" +
                                                        "Age: %s\n" +
                                                        "Language: %s" +
                                                        "Mood: %s\n" +
                                                        "Goals: %s\n" +
                                                        "Interests: %s\n" +
                                                        "Make it sound %s.\n",
                                        user.getName(), user.getLanguage(),
                                        user.getGoals(),
                                        // user.getInterests(),
                                        user.getTone_preference());

                        // return "you reached geminiservice";

                        return geminiClient.models
                                        .generateContent("gemini-2.5-flash", prompt, null)
                                        .text();
                }

        }

        // -------------------------------- TTS
        // -------------------------------------------

        // Step 2: Convert affirmation text to .wav
        public byte[] generateTTS(String affirmationText) {
                GenerateContentResponse response = geminiClient.models
                                .generateContent(
                                                "gemini-2.5-flash-preview-tts",
                                                List.of(
                                                                Content.builder()
                                                                                .role("user")
                                                                                .parts(List.of(Part.fromText(
                                                                                                affirmationText)))
                                                                                .build()),
                                                GenerateContentConfig.builder()
                                                                .responseModalities(List.of("AUDIO"))
                                                                .speechConfig(
                                                                                SpeechConfig.builder()
                                                                                                .voiceConfig(
                                                                                                                VoiceConfig.builder()
                                                                                                                                .prebuiltVoiceConfig(
                                                                                                                                                PrebuiltVoiceConfig
                                                                                                                                                                .builder()
                                                                                                                                                                .voiceName("Kore")
                                                                                                                                                                .build())
                                                                                                                                .build())
                                                                                                .build())
                                                                .build());

                // Extract audio bytes directly
                byte[] audioBytes = response.candidates().get().get(0)
                                .content().get()
                                .parts().get().get(0)
                                .inlineData().get() // unwrap Optional<Blob>
                                .data().get(); // unwrap Optional<byte[]>

                System.out.println("reached till .wav file generation here=============================\n");
                // Save to file in main/java/audioFiles
                try {
                        java.nio.file.Path dirPath = java.nio.file.Paths
                                        .get("daily-vibe/src/main/java/com/dailyvibeproj/daily_vibe/audioFiles");
                        java.nio.file.Files.createDirectories(dirPath);
                        // Use a unique filename, e.g., current timestamp
                        // String filename = "affirmation_" + System.currentTimeMillis() + ".wav";
                        String filename = "affirmation.wav";
                        java.nio.file.Path filePath = dirPath.resolve(filename);
                        java.nio.file.Files.write(filePath, audioBytes);
                        System.out.println("SAVED .wav FILE IN TRY-CATCH here=============================\n");

                } catch (java.io.IOException e) {
                        e.printStackTrace();
                }

                return audioBytes;
        }

}
