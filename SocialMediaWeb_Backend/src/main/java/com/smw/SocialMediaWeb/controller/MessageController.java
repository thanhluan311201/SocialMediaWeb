package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.*;
import com.smw.SocialMediaWeb.dto.response.FriendResponse;
import com.smw.SocialMediaWeb.dto.response.MessageResponse;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.entity.Message;
import com.smw.SocialMediaWeb.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;
    NotificationService notificationService;


    @PostMapping("")
    ApiResponse<MessageResponse> sendMessage(@RequestBody MessageRequest request){
        var result = messageService.sendMessage(request);

        notificationService.sendNotification(result.getMessageId(), "MESSAGE");

        return ApiResponse.<MessageResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{messageId}")
    ApiResponse<MessageResponse> updateMessage(@PathVariable String messageId,
                                                  @RequestBody MessageUpdateRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.updateMessage(messageId, request))
                .build();
    }

    @DeleteMapping("/{messageId}")
    String deleteMessage(@PathVariable String messageId){
        messageService.deleteMessage(messageId);

        return "Message has been deleted";
    }
}
