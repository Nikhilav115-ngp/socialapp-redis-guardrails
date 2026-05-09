package com.nikhil.socialapp.service;

import com.nikhil.socialapp.entity.Comment;
import com.nikhil.socialapp.entity.Post;
import com.nikhil.socialapp.entity.User;
import com.nikhil.socialapp.repository.CommentRepository;
import com.nikhil.socialapp.repository.PostRepository;
import com.nikhil.socialapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    // Add Comment
    public Comment addComment(Long postId, Long userId, String content, int depthLevel) {

        // -------------------------
        // 1. Vertical Cap Check
        // -------------------------
        if (depthLevel > 20) {
            throw new RuntimeException("Max comment depth exceeded (20)");
        }

        // -------------------------
        // 2. Fetch Post
        // -------------------------
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // -------------------------
        // 3. Fetch User
        // -------------------------
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // -------------------------
        // 4. Create Comment
        // -------------------------
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDepthLevel(depthLevel);
        comment.setPost(post);
        comment.setAuthor(user);

        Comment savedComment = commentRepository.save(comment);

        // -------------------------
        // 5. Redis Virality Update
        // -------------------------
        redisService.increaseVirality(postId, 50);

        return savedComment;
    }
}