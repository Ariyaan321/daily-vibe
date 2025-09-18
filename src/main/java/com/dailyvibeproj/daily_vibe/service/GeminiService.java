package com.dailyvibeproj.daily_vibe.service;

import com.dailyvibeproj.daily_vibe.model.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.SpeechConfig;
import com.google.genai.types.VoiceConfig;
import com.google.genai.types.PrebuiltVoiceConfig;

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
                                                        // "Age: %s\n" +
                                                        "Language: %s" +
                                                        // "Mood: %s\n" +
                                                        "Goals: %s\n" +
                                                        // "Interests: %s\n" +
                                                        "Make it sound %s.\n",
                                        user.getName(), user.getLanguage(),
                                        user.getGoals(),
                                        user.getTone_preference());

                        return geminiClient.models
                                        .generateContent("gemini-2.5-flash", prompt, null)
                                        .text();
                }

        }

        // -------------------------------- TTS
        // -------------------------------------------

        public byte[] generateTTS(String affirmationText) throws IOException {
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

                // 1. Get the RAW audio bytes from the API response
                byte[] rawPcmAudio = response.candidates().get().get(0)
                                .content().get()
                                .parts().get().get(0)
                                .inlineData().get()
                                .data().get();

                System.out.println("Successfully received raw audio bytes from Gemini API.");

                // 2. Add the necessary WAV header to make it a playable file
                byte[] wavFileBytes = addWavHeader(rawPcmAudio);

                System.out.println("WAV header added. Returning complete audio file to controller.");

                // New / modified part:
                Path outputPath = Paths.get("src/main/resources/audioFiles/affirmation.wav");
                // Ensure the directory exists
                Files.createDirectories(outputPath.getParent());
                // Write file
                Files.write(outputPath, wavFileBytes);
                System.out.println("Audio file saved at: " + outputPath.toAbsolutePath());

                return wavFileBytes;
        }

        /**
         * Wraps raw PCM audio data with a standard 44-byte WAV header.
         * The Gemini TTS API provides audio at 24000 Hz, 16-bit, mono.
         *
         * @param pcmData The raw audio bytes from the API.
         * @return A byte array representing a complete and playable .wav file.
         * @throws IOException if there's an error writing the header data.
         */
        private byte[] addWavHeader(byte[] pcmData) throws IOException {
                long byteRate = 24000 * 2; // SampleRate * BitsPerSample * Channels / 8
                long totalDataLen = pcmData.length + 36;

                ByteArrayOutputStream header = new ByteArrayOutputStream();
                header.writeBytes("RIFF".getBytes()); // RIFF header
                header.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt((int) totalDataLen)
                                .array());
                header.writeBytes("WAVE".getBytes()); // WAVE header
                header.writeBytes("fmt ".getBytes()); // format chunk
                header.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(16).array()); // 16 for
                                                                                                             // PCM
                header.writeBytes(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 1).array()); // PCM
                                                                                                                      // format
                header.writeBytes(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 1).array()); // 1
                                                                                                                      // channel
                                                                                                                      // (mono)
                header.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(24000).array()); // sample
                                                                                                                // rate
                header.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt((int) byteRate).array()); // byte
                                                                                                                         // rate
                header.writeBytes(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 2).array()); // block
                                                                                                                      // align
                header.writeBytes(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 16).array()); // bits
                                                                                                                       // per
                                                                                                                       // sample
                header.writeBytes("data".getBytes()); // data chunk header
                header.writeBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(pcmData.length).array()); // data
                                                                                                                         // size

                // Combine header and PCM data
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(header.toByteArray());
                outputStream.write(pcmData);

                return outputStream.toByteArray();
        }

}
