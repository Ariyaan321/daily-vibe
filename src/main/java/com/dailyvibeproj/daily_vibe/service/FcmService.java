package com.dailyvibeproj.daily_vibe.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    // ... (keep the sendNotification to a single token method if you might need it later)

    /**
     * Sends a notification to a specific topic.
     *
     * @param topic The name of the topic to send to (e.g., "all_users").
     * @param title The title of the notification.
     * @param body  The body message of the notification.
     * @return The message ID from FCM.
     * @throws FirebaseMessagingException if sending the message fails.
     */
    public String sendNotificationToTopic(String topic, String title, String body) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build();

        // Send the message
        return FirebaseMessaging.getInstance().send(message);
    }
}