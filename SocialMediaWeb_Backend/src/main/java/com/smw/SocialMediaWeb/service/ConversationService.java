package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.repository.ConversationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;

    public Conversation createConversation(List<User> users){
        if (users == null || users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<String> userIds = users.stream()
                                    .map(User::getId)
                                    .toList();

        List<Conversation> conversations = conversationRepository.findByParticipantsIn(userIds);

        Optional<Conversation> existingConversation = conversations.stream()
                .filter(conversation -> conversation.getParticipants().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
                        .containsAll(userIds))
                .findFirst();

        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        Conversation conversation = Conversation.builder()
                .participants(new HashSet<>(users))
                .build();

        return conversationRepository.save(conversation);
    }
}
