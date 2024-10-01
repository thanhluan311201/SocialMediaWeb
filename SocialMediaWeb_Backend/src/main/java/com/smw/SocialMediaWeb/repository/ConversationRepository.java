package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Boolean existsByParticipantsIn(List<String> participantIds);
    List<Conversation> findByParticipantsIn(List<String> participantId);
}
