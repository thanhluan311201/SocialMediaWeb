package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.ResponseFriendRequest;
import com.smw.SocialMediaWeb.dto.response.FriendResponse;
import com.smw.SocialMediaWeb.entity.Friend;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.enums.FriendRequestStatus;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.FriendMapper;
import com.smw.SocialMediaWeb.repository.FriendRepository;
import com.smw.SocialMediaWeb.repository.FriendRequestRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendService {
    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;
    FriendMapper friendMapper;
    FriendRepository friendRepository;
    FriendRequestService friendRequestService;
    ConversationService conversationService;

    public FriendResponse responseFriendRequest(String friendRequestId, ResponseFriendRequest request){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friendRequest.getReceiver().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Friend friend1 = null;
        if(request.getStatus().equals(FriendRequestStatus.ACCEPTED)){
            friend1 = friendMapper.toFriend(request);
            friend1.setUser(currentUser);
            friend1.setFriend(friendRequest.getRequester());
            friend1.setAddedAt(LocalDateTime.now());
            friendRepository.save(friend1);

            Friend friend2 = friendMapper.toFriend(request);
            friend2.setUser(friendRequest.getRequester());
            friend2.setFriend(currentUser);
            friend2.setAddedAt(LocalDateTime.now());
            friendRepository.save(friend2);

            List<User> users = new ArrayList<>();
            users.add(currentUser);
            users.add(friendRequest.getRequester());

            conversationService.createConversation(users);
            friendRequestService.deleteFriendRequest(friendRequestId);
            return friendMapper.toFriendResponse(friend1);
        }

        friendRequestService.deleteFriendRequest(friendRequestId);
        return friendMapper.toFriendResponse(friend1);
    }
}
