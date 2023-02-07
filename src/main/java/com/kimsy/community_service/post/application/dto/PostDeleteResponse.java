package com.kimsy.community_service.post.application.dto;

import lombok.Getter;

@Getter
public class PostDeleteResponse {
    private final boolean deleted;

    public PostDeleteResponse(final boolean deleted) {
        this.deleted = deleted;
    }
}
