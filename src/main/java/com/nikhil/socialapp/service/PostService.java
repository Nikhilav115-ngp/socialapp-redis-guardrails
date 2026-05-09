package com.nikhil.socialapp.service;

import com.nikhil.socialapp.entity.Post;
import com.nikhil.socialapp.entity.User;
import com.nikhil.socialapp.repository.PostRepository;
import com.nikhil.socialapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // -------------------------
    // CREATE POST
    // -------------------------
    public Post createPost(Long userId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(user);
        post.setLikeCount(0);

        return postRepository.save(post);
    }

    // -------------------------
    // LIKE POST
    // -------------------------
    public Post likePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // safe increment
        post.setLikeCount(
        java.util.Optional.ofNullable(post.getLikeCount()).orElse(0) + 1
        );

        Post savedPost = postRepository.save(post);

        // Redis virality update (+20 for human like)
        redisService.increaseVirality(postId, 20);

        return savedPost;
    }
}