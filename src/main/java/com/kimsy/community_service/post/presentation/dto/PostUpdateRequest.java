package com.kimsy.community_service.post.presentation.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostUpdateRequest {
    private String title;
    private String contents;

    public PostUpdateRequest(final String title, final String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
