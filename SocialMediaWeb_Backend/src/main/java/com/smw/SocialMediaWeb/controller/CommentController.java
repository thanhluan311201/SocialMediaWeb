package com.smw.SocialMediaWeb.controller;

import com.smw.SocialMediaWeb.dto.request.ApiResponse;
import com.smw.SocialMediaWeb.dto.request.CommentCreationRequest;
import com.smw.SocialMediaWeb.dto.request.CommentUpdateRequest;
import com.smw.SocialMediaWeb.dto.response.CommentResponse;
import com.smw.SocialMediaWeb.entity.Post;
import com.smw.SocialMediaWeb.repository.PostRepository;
import com.smw.SocialMediaWeb.service.CommentService;
import com.smw.SocialMediaWeb.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;
    NotificationService notificationService;
    PostRepository postRepository;

    @PostMapping("/{objectId}")
    ApiResponse<CommentResponse> comment(@PathVariable String objectId,
                                          @RequestBody @Valid CommentCreationRequest request){

        var result = commentService.comment(objectId, request);

        Optional<Post> post = postRepository.findById(objectId);
        if (post.isPresent()){
            notificationService.sendNotification(result.getCommentId(), "COMMENT");
        }else {
            notificationService.sendNotification(result.getCommentId(), "REPLY_COMMENT");
        }

        return ApiResponse.<CommentResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{commentId}")
    ApiResponse<CommentResponse> updateComment(@PathVariable String commentId,
                                                        @RequestBody @Valid CommentUpdateRequest request){

        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId, request))
                .build();
    }

    @DeleteMapping("/{commentId}")
    String deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);

        return "Comment has been deleted";
    }
}
