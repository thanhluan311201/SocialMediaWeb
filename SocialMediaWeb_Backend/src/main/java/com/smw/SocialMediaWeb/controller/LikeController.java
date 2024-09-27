package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.dto.response.LikeResponse;
import com.smw.SocialMediaWeb.service.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {
    LikeService likeService;

    @PostMapping("/{objectId}")
    ApiResponse<LikeResponse> likePost(@PathVariable String objectId){

        return ApiResponse.<LikeResponse>builder()
                .result(likeService.likePost(objectId))
                .build();
    }

    @DeleteMapping("/{likeId}")
    String unLike(@PathVariable String likeId){
        likeService.unLike(likeId);

        return "unlike successful";
    }
}
