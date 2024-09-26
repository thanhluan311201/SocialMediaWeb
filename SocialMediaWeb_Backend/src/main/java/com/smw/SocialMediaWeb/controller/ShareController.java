package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.*;
import com.smw.SocialMediaWeb.dto.response.PostResponse;
import com.smw.SocialMediaWeb.dto.response.SharePostResponse;
import com.smw.SocialMediaWeb.entity.Share;
import com.smw.SocialMediaWeb.service.PostService;
import com.smw.SocialMediaWeb.service.ShareService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/share")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShareController {
    ShareService shareService;

    @PostMapping("/{postId}")
    ApiResponse<SharePostResponse> sharePost(@PathVariable String postId, @RequestBody @Valid SharePostRequest request){

        return ApiResponse.<SharePostResponse>builder()
                .result(shareService.sharePost(postId, request))
                .build();
    }

    @PutMapping("/{sharePostId}")
    ApiResponse<SharePostResponse> updatePost(@PathVariable String sharePostId,
                                              @RequestBody @Valid SharedPostUpdateRequest request){
        return ApiResponse.<SharePostResponse>builder()
                .result(shareService.updateSharedPost(sharePostId, request))
                .build();
    }

    @DeleteMapping("/{sharePostId}")
    String deletePost(@PathVariable String sharePostId){
        shareService.deleteSharedPost(sharePostId);

        return "Post has been deleted";
    }
}
