package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Friend;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
}
