package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.*;
import com.smw.SocialMediaWeb.dto.response.FriendResponse;
import com.smw.SocialMediaWeb.dto.response.MessageResponse;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.service.ConversationService;
import com.smw.SocialMediaWeb.service.FriendRequestService;
import com.smw.SocialMediaWeb.service.FriendService;
import com.smw.SocialMediaWeb.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @PostMapping("")
    ApiResponse<MessageResponse> sendMessage(@RequestBody MessageRequest request){
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.sendMessage(request))
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
