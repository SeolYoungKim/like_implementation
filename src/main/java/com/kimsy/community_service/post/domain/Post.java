package com.kimsy.community_service.post.domain;

import com.kimsy.community_service.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Post(final String title, final String contents, final Member author) {
        validateNullOrEmpty(title);
        validateNullOrEmpty(contents);

        this.title = title;
        this.contents = contents;
        this.author = author;
    }

    private void validateNullOrEmpty(final String str) {
        if (!StringUtils.hasText(str)) {
            throw new IllegalArgumentException("제목이나 내용은 빈 값일 수 없습니다.");
        }
    }

    public String getAuthorName() {
        return author.getNickname();
    }
}
