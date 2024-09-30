package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.ResponseFriendRequest;
import com.smw.SocialMediaWeb.dto.request.SendFriendRequest;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.entity.FriendRequest;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.enums.FriendRequestStatus;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.FriendMapper;
import com.smw.SocialMediaWeb.repository.FriendRequestRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendRequestService {
    FriendRequestRepository friendRequestRepository;
    UserRepository userRepository;
    FriendMapper friendMapper;

    public SendFriendRequestResponse sendFriendRequest(SendFriendRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(user == receiver){
            throw new AppException(ErrorCode.INVALID_FRIEND_REQUEST);
        }


        LocalDateTime localDateTime = LocalDateTime.now();

        FriendRequest friendRequest = friendMapper.toFriendRequest(request);
        friendRequest.setRequester(user);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestTime(localDateTime);
        friendRequest.setStatus(FriendRequestStatus.PENDING);

        return friendMapper.toSendFriendRequestResponse(friendRequestRepository.save(friendRequest));
    }

    public void deleteFriendRequest(String friendRequestId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friendRequest.getRequester().getId().equals(currentUser.getId()) &&
                !friendRequest.getReceiver().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        friendRequestRepository.deleteById(friendRequestId);
    }
}
