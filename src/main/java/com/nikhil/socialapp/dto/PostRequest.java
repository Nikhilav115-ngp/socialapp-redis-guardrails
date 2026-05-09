package com.nikhil.socialapp.dto;

public class PostRequest {

    private Long userId;
    private String content;

    // constructors (optional but good practice)
    public PostRequest() {}

    public PostRequest(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    // getters & setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}