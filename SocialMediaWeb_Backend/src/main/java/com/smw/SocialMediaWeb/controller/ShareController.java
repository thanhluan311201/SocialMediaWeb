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
    ApiResponse<SharePostResponse> sharePost(@PathVariable String postId,
                                             @RequestBody @Valid SharePostRequest request){
        return ApiResponse.<SharePostResponse>builder()
                .result(shareService.sharePost(postId, request))
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
