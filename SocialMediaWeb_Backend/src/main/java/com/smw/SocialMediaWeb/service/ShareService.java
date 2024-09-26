package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.PostCreationRequest;
import com.smw.SocialMediaWeb.dto.request.PostUpdateRequest;
import com.smw.SocialMediaWeb.dto.request.SharePostRequest;
import com.smw.SocialMediaWeb.dto.request.SharedPostUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.PostResponse;
import com.smw.SocialMediaWeb.dto.response.SharePostResponse;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.Share;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.PostMapper;
import com.smw.SocialMediaWeb.mapper.ShareMapper;
import com.smw.SocialMediaWeb.repository.PostRepository;
import com.smw.SocialMediaWeb.repository.ShareRepository;
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
public class ShareService {
    ShareRepository shareRepository;
    ShareMapper shareMapper;
    UserRepository userRepository;
    PostRepository postRepository;

    public SharePostResponse sharePost(String postId, SharePostRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Share share = shareMapper.toShare(request);
        LocalDateTime localDateTime = LocalDateTime.now();
        share.setSharedAt(localDateTime);
        share.setUser(user);
        share.setPost(post);

        return shareMapper.toSharePostResponse(shareRepository.save(share));
    }

    public SharePostResponse updateSharedPost(String sharedPostId, SharedPostUpdateRequest request){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Share share = shareRepository.findById(sharedPostId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!share.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        shareMapper.updateSharedPost(share, request);

        return shareMapper.toSharePostResponse(shareRepository.save(share));
    }

    public void deleteSharedPost(String sharedPostId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Share share = shareRepository.findById(sharedPostId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!share.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        shareRepository.deleteById(sharedPostId);
    }
}
