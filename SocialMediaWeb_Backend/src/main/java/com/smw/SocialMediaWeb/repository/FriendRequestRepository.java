package com.smw.SocialMediaWeb.repository;

import com.smw.SocialMediaWeb.entity.Friend;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    List<FriendRequest> findFriendRequestsByReceiverId(String userId, Sort sort);
}
