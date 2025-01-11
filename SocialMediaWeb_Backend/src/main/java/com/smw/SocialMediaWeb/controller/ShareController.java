package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.*;
import com.smw.SocialMediaWeb.dto.response.PostResponse;
import com.smw.SocialMediaWeb.dto.response.SharePostResponse;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.Share;
import com.smw.SocialMediaWeb.service.NotificationService;
import com.smw.SocialMediaWeb.service.PostService;
import com.smw.SocialMediaWeb.service.ShareService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/share")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShareController {
    ShareService shareService;
    NotificationService notificationService;


    @GetMapping
    ApiResponse<List<Share>> getSharedPost(){
        return ApiResponse.<List<Share>>builder()
                .result(shareService.getSharedPosts())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<List<Share>> getSharedPostByUser(@PathVariable String userId){
        return ApiResponse.<List<Share>>builder()
                .result(shareService.getSharedPostsByUser(userId))
                .build();
    }

    @PostMapping("/{postId}")
    ApiResponse<SharePostResponse> sharePost(@PathVariable String postId,
                                             @RequestBody @Valid SharePostRequest request){

        var result = shareService.sharePost(postId, request);

        notificationService.sendNotification(result.getShareId(), "SHARE");

        return ApiResponse.<SharePostResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{sharedPostId}")
    ApiResponse<SharePostResponse> updatePost(@PathVariable String sharedPostId,
                                              @RequestBody @Valid SharedPostUpdateRequest request){
        return ApiResponse.<SharePostResponse>builder()
                .result(shareService.updateSharedPost(sharedPostId, request))
                .build();
    }

    @DeleteMapping("/{sharedPostId}")
    String deletePost(@PathVariable String sharedPostId){
        shareService.deleteSharedPost(sharedPostId);

        return "Post has been deleted";
    }
}
