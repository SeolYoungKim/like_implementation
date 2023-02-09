package com.kimsy.community_service.post.application.dto;

import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsPageResponse {
    public static PostsPageResponse from(final Post post) {
        return PostsPageResponse.from(post, null);
    }

    public static PostsPageResponse from(final Post post, final LikeStatus likeStatus) {
        return PostsPageResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(String.format("%s(%s)", post.getAuthorName(), post.getKorAccountType()))
                .likes(post.getLikesCount())
                .likeStatus(likeStatus)
                .createdAt(post.getCreatedAt())
                .build();
    }

    private final Long id;
    private final String title;
    private final String author;
    private final Long likes;
    private final LikeStatus likeStatus;
    private final LocalDateTime createdAt;

    @Builder
    public PostsPageResponse(
            final Long id,
            final String title,
            final String author,
            final Long likes,
            final LikeStatus likeStatus,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.likes = likes;
        this.likeStatus = likeStatus;
        this.createdAt = createdAt;
    }
}
