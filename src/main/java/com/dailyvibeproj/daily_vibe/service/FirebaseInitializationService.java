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

    @Value("${firebase.api.key}")
    private String firebaseApiKey;

    @PostConstruct // This ensures this method is run after the bean is initialized
    public void initialize() {
        try {
            // Load the service account key file from the classpath
            InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream(firebaseApiKey);

            if (serviceAccount == null) {
                throw new RuntimeException("Firebase service account key file not found in classpath.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Initialize Firebase if not already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase application has been initialized");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}