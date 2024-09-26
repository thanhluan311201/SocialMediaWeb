package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Optional<Message> findByContent(String Content);
}
