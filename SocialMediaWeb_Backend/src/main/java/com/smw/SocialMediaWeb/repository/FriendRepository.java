package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
}
