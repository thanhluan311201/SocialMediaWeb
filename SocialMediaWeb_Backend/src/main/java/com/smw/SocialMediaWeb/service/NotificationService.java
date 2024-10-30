package com.smw.SocialMediaWeb.service;

import com.smw.SocialMediaWeb.controller.NotificationController;
import com.smw.SocialMediaWeb.entity.*;
import com.smw.SocialMediaWeb.enums.DefaultNotification;
import com.smw.SocialMediaWeb.enums.NotificationType;
import com.smw.SocialMediaWeb.exception.AppException;
import com.smw.SocialMediaWeb.exception.ErrorCode;
import com.smw.SocialMediaWeb.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    UserRepository userRepository;
    NotificationRepository notificationRepository;
    PostRepository postRepository;
    ShareRepository shareRepository;
    CommentRepository commentRepository;
    MessageRepository messageRepository;
    SimpMessagingTemplate messagingTemplate;

    public List<Notification> getNotificationsForUser(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return notificationRepository.findByReceiverIdAndIsReadFalse(currentUser.getId());
    }

    public void sendNotification(String objectId, String type) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<Post> postOptional = postRepository.findById(objectId);
        Optional<Share> shareOptional = shareRepository.findById(objectId);
        Optional<Comment> commentOptional = commentRepository.findById(objectId);
        Optional<Message> messageOptional = messageRepository.findById(objectId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            String content = currentUser.getFirstname() + DefaultNotification.POST_LIKE_NOTIFICATION.getMessage();
            String redirectUrl = "/post/" + post.getId();
            createAndSaveNotification(post.getAuthor(), content, redirectUrl, NotificationType.LIKE);
        }

        if (shareOptional.isPresent()) {
            Share share = shareOptional.get();
            String content = currentUser.getFirstname() + DefaultNotification.POST_SHARE_NOTIFICATION;
            String redirectUrl = "/share/" + share.getId();
            createAndSaveNotification(share.getPost().getAuthor(), content, redirectUrl, NotificationType.SHARE);
        }

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            String content = "";
            String redirectUrl = "/post/" + comment.getPost().getId();
            NotificationType notificationType = null;
            User receiver = null;

            switch (type) {
                case "LIKE":
                    content = currentUser.getFirstname() + DefaultNotification.COMMENT_LIKE_NOTIFICATION.getMessage();
                    notificationType = NotificationType.LIKE;
                    receiver = comment.getUser();
                    break;
                case "COMMENT":
                    content = currentUser.getFirstname() + DefaultNotification.POST_COMMENT_NOTIFICATION.getMessage();
                    notificationType = NotificationType.COMMENT;
                    receiver = comment.getPost().getAuthor();
                    break;
                case "REPLY_COMMENT":
                    content = currentUser.getFirstname() + DefaultNotification.COMMENT_RESPONSE_NOTIFICATION.getMessage();
                    notificationType = NotificationType.REPLY_COMMENT;
                    receiver = comment.getParentComment().getUser();
                    break;
                default:
                    throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }

            createAndSaveNotification(receiver, content, redirectUrl, notificationType);
        }

        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            String content = currentUser.getFirstname() + DefaultNotification.MESSAGE_NOTIFICATION.getMessage();
            String redirectUrl = "/conversation/" + message.getConversation().getId();
            createAndSaveNotification(message.getReceiver(), content, redirectUrl, NotificationType.MESSAGE);
        }
    }

    private void createAndSaveNotification(User receiver, String content, String redirectUrl, NotificationType type) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setReceiver(receiver);
        notification.setRedirectUrl(redirectUrl);
        notification.setType(type);
        notification.setCreatedAt(new Date());
        notification.setRead(false);

        notificationRepository.save(notification);

        sendNotificationToUser(receiver, notification);
    }

    private void sendNotificationToUser(User receiver, Notification notification){
        messagingTemplate.convertAndSendToUser(receiver.getId(),"/notification", notification);
    }
}
