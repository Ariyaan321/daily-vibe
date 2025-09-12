package com.dailyvibeproj.daily_vibe.service;

import com.dailyvibeproj.daily_vibe.model.NotificationRequest;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
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

     public void sendMessageToToken(NotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        // logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response+ " msg "+jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic).build()).build();
    }
    
    private Message getPreconfiguredMessageToToken(NotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(NotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        // ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        Notification notification = Notification.builder()
                                        .setTitle(request.getTitle())
                                        .setBody(request.getBody())
                                        .build();
        return Message.builder()
                .setAndroidConfig(androidConfig).setNotification(notification);
    }
    

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