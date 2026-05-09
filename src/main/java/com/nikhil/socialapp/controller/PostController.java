package com.nikhil.socialapp.controller;

import com.nikhil.socialapp.dto.PostRequest;
import com.nikhil.socialapp.entity.Post;
import com.nikhil.socialapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Create Post
    @PostMapping
    public Post createPost(@RequestBody PostRequest request) {

        return postService.createPost(
                request.getUserId(),
                request.getContent()
        );
    }

    // Like Post
    @PostMapping("/{postId}/like")
    public Post likePost(@PathVariable Long postId) {
        return postService.likePost(postId);
    }
}