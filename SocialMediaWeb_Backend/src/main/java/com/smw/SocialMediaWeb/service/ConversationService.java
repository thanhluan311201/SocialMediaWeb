package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.repository.ConversationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;

    public void createConversation(Set<User> users){
        Conversation conversation = Conversation
                .builder()
                .participants(users)
                .build();

        conversationRepository.save(conversation);
    }
}
