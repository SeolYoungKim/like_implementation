package com.kimsy.community_service.post.application.dto;

import com.kimsy.community_service.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsPageResponse {
    public static PostsPageResponse from(final Post post) {
        return PostsPageResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(String.format("%s(%s)", post.getAuthorName(), post.getKorAccountType()))
                .createdAt(post.getCreatedAt())
                .likes(post.getLikesCount())
                .build();
    }

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;
    private final Long likes;

    @Builder
    public PostsPageResponse(
            final Long id,
            final String title,
            final String author,
            final LocalDateTime createdAt,
            final Long likes
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.likes = likes;
    }
}
