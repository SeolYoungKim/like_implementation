package com.kimsy.community_service.post.domain;

import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.like.domain.Likes;
import com.kimsy.community_service.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "post")
    private final List<Likes> likes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Delete delete;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    public Post(final String title, final String contents, final Member author, final Delete delete) {
        validateNullOrEmpty(title);
        validateNullOrEmpty(contents);

        this.title = title;
        this.contents = contents;
        this.author = author;
        this.delete = delete;
    }

    private void validateNullOrEmpty(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("제목이나 내용은 빈 값일 수 없습니다.");
        }
    }

    public String getAuthorName() {
        return author.getNickname();
    }

    public String getKorAccountType() {
        return author.getKorAccountType();
    }

    public void validateAuthor(final Member member) {
        if (!author.equals(member)) {
            throw new IllegalArgumentException("게시글 작성자가 아니면 수정/삭제 요청을 할 수 없습니다.");
        }
    }

    public void update(final String title, final String contents) {
        validateNullOrEmpty(title);
        validateNullOrEmpty(contents);

        this.title = title;
        this.contents = contents;
    }

    public void delete() {
        validateDeletedAlready();
        delete = Delete.YES;
        deletedAt = LocalDateTime.now();
    }

    private void validateDeletedAlready() {
        if (delete == Delete.YES) {
            throw new IllegalArgumentException("이미 삭제된 게시글 입니다.");
        }
    }

    public void addLikes(final Likes likes) {
        this.likes.add(likes);
    }

    public Long getLikesCount() {
        return likes.stream()
                .filter(likesEntity -> likesEntity.getLikeStatus() == LikeStatus.LIKE)
                .count();
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

    public Member getAuthor() {
        return author;
    }

    public List<Likes> getLikes() {
        return Collections.unmodifiableList(likes);
    }

    public Delete getDelete() {
        return delete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", author=" + author +
                ", delete=" + delete +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
