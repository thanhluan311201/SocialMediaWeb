package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Conversation;
import com.smw.SocialMediaWeb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Boolean existsByParticipantsIn(List<String> participantIds);
    List<Conversation> findByParticipantsIn(List<String> participantId);
    List<Conversation> findByParticipants(User participant);
}
