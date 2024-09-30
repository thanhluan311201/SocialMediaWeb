package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.response.LikeResponse;
import com.smw.SocialMediaWeb.entity.*;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.LikeMapper;
import com.smw.SocialMediaWeb.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeService {
    LikeRepository likeRepository;
    LikeMapper likePostMapper;
    PostRepository postRepository;
    ShareRepository shareRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;

    public LikeResponse likePost(String objectId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<Post> postOptional = postRepository.findById(objectId);
        Optional<Share> shareOptional = shareRepository.findById(objectId);
        Optional<Comment> commentOptional = commentRepository.findById(objectId);

        if(postOptional.isEmpty() && shareOptional.isEmpty() && commentOptional.isEmpty()){

            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }


        if(postOptional.isPresent()){
            Post post = postOptional.get();
            ObjectLike postLike = ObjectLike
                    .builder()
                    .post(post)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .build();

            return likePostMapper.toPostLikeResponse(likeRepository.save(postLike));
        } else if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            ObjectLike postLike = ObjectLike
                    .builder()
                    .share(share)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .build();

            return likePostMapper.toShareLikeResponse(likeRepository.save(postLike));
        } else {
            Comment comment = commentOptional.get();
            ObjectLike commentLike = ObjectLike
                    .builder()
                    .comment(comment)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .build();

            return likePostMapper.toCommentLikeResponse(likeRepository.save(commentLike));
        }
    }

    public void unLike(String likeId){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ObjectLike like = likeRepository.findById(likeId)
                .orElseThrow(() ->new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        if (!like.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        likeRepository.deleteById(likeId);
    }

    public Integer countLike(String objectId){
        Optional<Post> postOptional = postRepository.findById(objectId);
        Optional<Share> shareOptional = shareRepository.findById(objectId);
        Optional<Comment> commentOptional = commentRepository.findById(objectId);
        if (postOptional.isPresent()){
            return Math.toIntExact(postOptional.stream().count());
        } else if (shareOptional.isPresent()) {
            return Math.toIntExact(shareOptional.stream().count());
        }else {
            return Math.toIntExact(commentOptional.stream().count());
        }
    }
}
