package com.smw.SocialMediaWeb.controller;


import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.entity.Notification;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    SimpMessagingTemplate messagingTemplate;
    NotificationService notificationService;

    @GetMapping("/notifications")
    public ApiResponse<List<Notification>> getNotifications() {

        return ApiResponse.<List<Notification>>builder()
                .result(notificationService.getNotificationsForUser())
                .build();
    }
}
