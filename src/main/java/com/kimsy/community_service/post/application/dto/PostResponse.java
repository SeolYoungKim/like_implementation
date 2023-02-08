package com.kimsy.community_service.post.application.dto;

import com.kimsy.community_service.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {
    public static PostResponse from(final Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .author(String.format("%s(%s)", post.getAuthorName(), post.getAccountType()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    private final Long id;
    private final String title;
    private final String contents;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    public PostResponse(
            final Long id,
            final String title,
            final String contents,
            final String author,
            final LocalDateTime createdAt,
            final LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
