package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Message;
import com.smw.SocialMediaWeb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversationId(String conversationId);
    Optional<Message> findByContent(String content);
    Optional<Message> findBySenderAndReceiver(User sender, User receiver);
}
