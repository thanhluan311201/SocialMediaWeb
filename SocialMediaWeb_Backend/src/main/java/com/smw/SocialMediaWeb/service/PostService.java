package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.PostCreationRequest;
import com.smw.SocialMediaWeb.dto.request.PostUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PostResponse;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.PostMapper;
import com.smw.SocialMediaWeb.repository.PostRepository;
import com.smw.SocialMediaWeb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;

    public PostResponse createPost(PostCreationRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postMapper.toPost(request);
        LocalDateTime localDateTime = LocalDateTime.now();
        post.setCreatedAt(localDateTime);
        post.setAuthor(user);

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public PostResponse updatePost(String postId, PostUpdateRequest request){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        postMapper.updatePost(post, request);

        return postMapper.toPostResponse(postRepository.save(post));
    }

    public void deletePost(String postId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        postRepository.deleteById(postId);
    }
}
