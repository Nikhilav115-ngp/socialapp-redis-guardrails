package com.nikhil.socialapp.service;

import com.nikhil.socialapp.entity.Comment;
import com.nikhil.socialapp.entity.Post;
import com.nikhil.socialapp.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikhil.socialapp.repository.CommentRepository;
import com.nikhil.socialapp.repository.PostRepository;
import com.nikhil.socialapp.repository.UserRepository;

@Service
public class BotService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public String processBotReply(Long postId, Long botId, Long userId) {

        // 1. CHECK COOLDOWN
        if (redisService.isCooldownActive(botId, userId)) {
            return "Rejected: Cooldown active";
        }

        // 2. CHECK USER
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. CHECK POST
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 4. NOW INCREASE COUNT
        long count = redisService.safeIncreaseBotCount(postId);

        if (count == -1) {
            return "Rejected: Bot limit reached";
        }

        // 5. SET COOLDOWN
        redisService.setCooldown(botId, userId);

        // 6. VIRALITY
        redisService.increaseVirality(postId, 1);

        // 7. SAVE COMMENT
        Comment comment = new Comment();

        comment.setContent("Bot auto reply");
        comment.setDepthLevel(1);
        comment.setPost(post);
        comment.setAuthor(user);

        commentRepository.save(comment);

        // 8. SEND NOTIFICATION
        notificationService.sendNotification(
                userId,
                "Bot replied to your post");

        return "Bot reply accepted";

    }
}