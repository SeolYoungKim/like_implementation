package com.kimsy.community_service.post.application.dto;

import com.kimsy.community_service.post.domain.Delete;
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
                .author(String.format("%s(%s)", post.getAuthorName(), post.getKorAccountType()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .delete(post.getDelete())
                .deletedAt(post.getDeletedAt())
                .build();
    }

    private final Long id;
    private final String title;
    private final String contents;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Delete delete;
    private final LocalDateTime deletedAt;

    @Builder
    public PostResponse(
            final Long id,
            final String title,
            final String contents,
            final String author,
            final LocalDateTime createdAt,
            final LocalDateTime modifiedAt,
            final Delete delete,
            final LocalDateTime deletedAt
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.delete = delete;
        this.deletedAt = deletedAt;
    }
}
