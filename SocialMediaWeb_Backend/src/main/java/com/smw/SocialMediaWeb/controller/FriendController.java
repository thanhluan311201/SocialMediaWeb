package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.dto.request.ResponseFriendRequest;
import com.smw.SocialMediaWeb.dto.request.SendFriendRequest;
import com.smw.SocialMediaWeb.dto.response.FriendResponse;
import com.smw.SocialMediaWeb.dto.response.SendFriendRequestResponse;
import com.smw.SocialMediaWeb.service.FriendRequestService;
import com.smw.SocialMediaWeb.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendController {
    FriendService friendService;
    FriendRequestService friendRequestService;

    @PostMapping()
    ApiResponse<SendFriendRequestResponse> sendFriendRequest(@RequestBody SendFriendRequest request){
        return ApiResponse.<SendFriendRequestResponse>builder()
                .result(friendRequestService.sendFriendRequest(request))
                .build();
    }

    @PostMapping("/{friendRequestId}")
    ApiResponse<FriendResponse> sendFriendRequest(@PathVariable String friendRequestId,
                                                  @RequestBody ResponseFriendRequest request) {
        return ApiResponse.<FriendResponse>builder()
                .result(friendService.responseFriendRequest(friendRequestId, request))
                .build();
    }
}
