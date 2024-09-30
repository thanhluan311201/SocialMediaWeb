package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.dto.request.CommentCreationRequest;
import com.smw.SocialMediaWeb.dto.request.CommentUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.CommentResponse;
import com.smw.SocialMediaWeb.entity.Comment;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.entity.Share;
import com.smw.SocialMediaWeb.entity.User;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.mapper.CommentMapper;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentMapper commentMapper;
    PostRepository postRepository;
    ShareRepository shareRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;

    public CommentResponse comment(String objectId, CommentCreationRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<Post> postOptional = postRepository.findById(objectId);
        Optional<Share> shareOptional = shareRepository.findById(objectId);
        Optional<Comment> commentOptional = commentRepository.findById(objectId);
        LocalDateTime localDateTime = LocalDateTime.now();

        if(postOptional.isEmpty() && shareOptional.isEmpty() && commentOptional.isEmpty()){
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        if (postOptional.isPresent()){
            Post post = postOptional.get();
            Comment comment = Comment
                    .builder()
                    .post(post)
                    .user(user)
                    .content(request.getContent())
                    .commentedAt(localDateTime)
                    .build();

            return commentMapper.toPostCommentResponse(commentRepository.save(comment));
        } else if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            Comment comment = Comment
                    .builder()
                    .share(share)
                    .user(user)
                    .content(request.getContent())
                    .commentedAt(localDateTime)
                    .build();

            return commentMapper.toShareCommentResponse(commentRepository.save(comment));
        }else {
            Comment parentComment = commentOptional.get();
            Comment comment = Comment
                    .builder()
                    .parentComment(parentComment)
                    .user(user)
                    .content(request.getContent())
                    .commentedAt(localDateTime)
                    .build();

            return commentMapper.toCommentReplyResponse(commentRepository.save(comment));
        }
    }

    public CommentResponse updateComment(String commentId, CommentUpdateRequest request){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentMapper.updateComment(comment, request);

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public void deleteComment(String commentId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }
}
