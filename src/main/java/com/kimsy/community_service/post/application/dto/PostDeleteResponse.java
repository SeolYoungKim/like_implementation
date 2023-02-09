package com.kimsy.community_service.post.application.dto;

public class PostDeleteResponse {
    private final boolean deleted;

    public PostDeleteResponse(final boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
