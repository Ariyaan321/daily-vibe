package com.dailyvibeproj.daily_vibe.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class FirebaseInitializationService {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    public FirebaseInitializationService() {
        System.out.println(">>> FirebaseInitializationService constructor called");
    }

    @PostConstruct
    public void initialize() {
        System.out.println(">>> Firebase config path: " + firebaseConfigPath);
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(firebaseConfigPath)) {
            if (serviceAccount == null) {
                throw new RuntimeException(
                        "Firebase service account key file not found in classpath: " + firebaseConfigPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase application has been initialized");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
        }
    }
}
