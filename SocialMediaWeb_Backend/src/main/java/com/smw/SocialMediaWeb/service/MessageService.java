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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    NotificationService notificationService;
    SimpMessagingTemplate messagingTemplate;

    public List<Message> getMessageByConversation(String conversationId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));

        if (!isParticipant) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<Message> allMessages = messageRepository.findByConversationId(conversationId);
        allMessages.sort(Comparator.comparing(Message::getSentAt));

        // Lọc tin nhắn theo người gửi hiện tại
        return allMessages.stream()
                .filter(message ->
                        (message.getSender().equals(currentUser)) ||
                        (message.getReceiver().equals(currentUser)))
                .collect(Collectors.toList());
    }

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

        Conversation conversation = conversationService.createConversation(users);

        Message message = messageMapper.toMessage(request);
        message.setSender(currentUser);
        message.setReceiver(receiver);
        message.setConversation(conversation);
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);

        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSendToUser(receiver.getId(),"/message", message);

        Set<Message> messages = new HashSet<>();
        messages.add(message);
        conversation.setMessages(messages);
        conversationRepository.save(conversation);

        return messageResponse;
    }

    public MessageResponse updateMessage(String messageId, MessageUpdateRequest request){
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!currentUser.equals(message.getSender())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        messageMapper.updateMessage(message, request);

        return messageMapper.toMessageResponse(messageRepository.save(message));
    }

    public void deleteMessage(String messageId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().equals(currentUser)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        messageRepository.delete(message);
    }
}
