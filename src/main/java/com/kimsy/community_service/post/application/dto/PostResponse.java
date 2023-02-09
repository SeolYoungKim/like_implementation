package com.kimsy.community_service.post.application.dto;

import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;

public class PostResponse {
    public static PostResponse from(final Post post) {
        return PostResponse.from(post, null);
    }

    public static PostResponse from(final Post post, final LikeStatus likeStatus) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .author(String.format("%s(%s)", post.getAuthorName(), post.getKorAccountType()))
                .likes(post.getLikesCount())
                .likeStatus(likeStatus)
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    private final Long id;
    private final String title;
    private final String contents;
    private final String author;
    private final Long likes;
    private final LikeStatus likeStatus;
    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    @Builder
    public PostResponse(
            final Long id,
            final String title,
            final String contents,
            final String author,
            final Long likes,
            final LikeStatus likeStatus, final LocalDateTime createdAt,
            final LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.likes = likes;
        this.likeStatus = likeStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getAuthor() {
        return author;
    }

    public Long getLikes() {
        return likes;
    }

    public LikeStatus getLikeStatus() {
        return likeStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
