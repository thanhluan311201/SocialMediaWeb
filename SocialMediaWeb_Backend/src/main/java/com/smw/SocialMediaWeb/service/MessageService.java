package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.MessageRequest;
import com.smw.SocialMediaWeb.dto.request.MessageUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.MessageResponse;
import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.Message;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.MessageMapper;
import com.smw.SocialMediaWeb.repository.ConversationRepository;
import com.smw.SocialMediaWeb.repository.MessageRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
    MessageRepository messageRepository;
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    ConversationService conversationService;
    MessageMapper messageMapper;


    public MessageResponse sendMessage(MessageRequest request){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(currentUser.equals(receiver)){
            throw new AppException(ErrorCode.INVALID_MESSAGE_RECEIVER);
        }

        List<User> users = Arrays.asList(currentUser, receiver);

        Conversation conversation =conversationService.createConversation(users);

        Message message1 = messageMapper.toMessage(request);
        message1.setSender(currentUser);
        message1.setReceiver(receiver);
        message1.setConversation(conversation);
        message1.setSentAt(LocalDateTime.now());
        messageRepository.save(message1);

        Message message2 = messageMapper.toMessage(request);
        message2.setSender(receiver);
        message2.setReceiver(currentUser);
        message2.setConversation(conversation);
        message2.setSentAt(LocalDateTime.now());
        messageRepository.save(message2);

        Set<Message> messages = new HashSet<>();
        messages.add(message1);
        messages.add(message2);

        conversation.setMessages(messages);
        conversationRepository.save(conversation);

        return messageMapper.toMessageResponse(message1);
    }

    public MessageResponse updateMessage(String messageId, MessageUpdateRequest request){
        Message message1 = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Message message2 = messageRepository.findBySenderAndReceiver(message1.getReceiver(), message1.getSender())
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        messageMapper.updateMessage(message1, request);
        messageMapper.updateMessage(message2, request);

        messageRepository.save(message1);
        messageRepository.save(message2);

        return messageMapper.toMessageResponse(message1);
    }

    public void deleteMessage(String messageId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Message message1 = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        Message message2 = messageRepository.findBySenderAndReceiver(message1.getReceiver(), message1.getSender())
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (message1.getSender().equals(currentUser)) {
            messageRepository.delete(message1);
            messageRepository.delete(message2);
        } else if (message1.getReceiver().equals(currentUser)) {
            messageRepository.delete(message2);
        }
    }
}
