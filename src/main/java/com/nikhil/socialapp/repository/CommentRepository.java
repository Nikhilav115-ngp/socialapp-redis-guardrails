package com.nikhil.socialapp.repository;

import com.nikhil.socialapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}