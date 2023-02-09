package com.kimsy.community_service.like.domain;

import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_post_and_number",
                        columnNames = {"post_id", "member_id"})
        }
)
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Likes(final Member member, final Post post, final LikeStatus likeStatus) {
        this.member = member;
        this.post = post;
        this.likeStatus = likeStatus;

        post.addLikes(this);
        member.addLikes(this);
    }

    public void updateStatusToDislike() {
        likeStatus = LikeStatus.DISLIKE;
    }

    public boolean isDislike() {
        return likeStatus == LikeStatus.DISLIKE;
    }

    public void updateStatusToLike() {
        likeStatus = LikeStatus.LIKE;
    }
}
