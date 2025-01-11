package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.Message;
import com.smw.SocialMediaWeb.service.ConversationService;
import com.smw.SocialMediaWeb.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;
    MessageService messageService;

    @GetMapping("")
    ApiResponse<List<Conversation>> getConversation(){

        return ApiResponse.<List<Conversation>>builder()
                .result(conversationService.getConversationByUser())
                .build();
    }

    @GetMapping("/{conversationId}")
    ApiResponse<List<Message>> getMessageBySender(@PathVariable String conversationId){
        return ApiResponse.<List<Message>>builder()
                .result(messageService.getMessageByConversation(conversationId))
                .build();
    }
}
