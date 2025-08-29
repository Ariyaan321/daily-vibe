package com.dailyvibeproj.daily_vibe.controller;
import com.dailyvibeproj.daily_vibe.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// A DTO for the broadcast request
record TopicNotificationRequest(String topic, String title, String body) {}

@RestController
public class NotificationController {

    @Autowired
    private FcmService fcmService;

    // ... (your existing endpoint for sending to a single token can remain)

    @PostMapping("/send-topic-notification")
    public String sendTopicNotification(@RequestBody TopicNotificationRequest request) {
        try {
            // Ensure the topic is the one you expect, or allow it from the request
            String topic = request.topic() != null ? request.topic() : "all_users";
            return fcmService.sendNotificationToTopic(topic, request.title(), request.body());
        } catch (FirebaseMessagingException e) {
            System.out.println(e.getMessage());
            return "Error sending notification: " + e.getMessage();
        }
    }
}