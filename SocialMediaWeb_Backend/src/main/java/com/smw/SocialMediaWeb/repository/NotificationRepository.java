package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByReceiverIdAndIsReadFalse(String receiverId);
}
