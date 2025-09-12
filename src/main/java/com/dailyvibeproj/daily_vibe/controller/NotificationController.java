package com.dailyvibeproj.daily_vibe.controller;
import com.dailyvibeproj.daily_vibe.model.NotificationRequest;
import com.dailyvibeproj.daily_vibe.model.NotificationResponse;
import com.dailyvibeproj.daily_vibe.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
public class NotificationController {
    @Autowired
    private FcmService fcmService;

    @PostMapping("/notification")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest request) throws ExecutionException, InterruptedException {
        fcmService.sendMessageToToken(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

}