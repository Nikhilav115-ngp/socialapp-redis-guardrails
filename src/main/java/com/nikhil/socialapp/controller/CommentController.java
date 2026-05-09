package com.nikhil.socialapp.controller;

import com.nikhil.socialapp.dto.CommentRequest;
import com.nikhil.socialapp.entity.Comment;
import com.nikhil.socialapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public Comment addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request
    ) {

        return commentService.addComment(
                postId,
                request.getUserId(),
                request.getContent(),
                request.getDepthLevel()
        );
    }
}