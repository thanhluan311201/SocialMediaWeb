package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.dto.request.PermissionRequest;
import com.smw.SocialMediaWeb.dto.request.PostCreationRequest;
import com.smw.SocialMediaWeb.dto.request.PostUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PermissionResponse;
import com.smw.SocialMediaWeb.dto.response.PostResponse;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @GetMapping
    ApiResponse<List<Post>> getPost(){
        return ApiResponse.<List<Post>>builder()
                .result(postService.getPosts())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<List<Post>> getPostsByUser(@PathVariable String userId){
        return ApiResponse.<List<Post>>builder()
                .result(postService.getPostsByUser(userId))
                .build();
    }

    @PostMapping
    ApiResponse<PostResponse> createPost(@RequestBody @Valid PostCreationRequest request){

        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @PutMapping("/{postId}")
    ApiResponse<PostResponse> updatePost(@PathVariable String postId, @RequestBody @Valid PostUpdateRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, request))
                .build();
    }

    @DeleteMapping("/{postId}")
    String deletePost(@PathVariable String postId){
        postService.deletePost(postId);

        return "Post has been deleted";
    }
}
