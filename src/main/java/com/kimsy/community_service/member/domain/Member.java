package com.kimsy.community_service.member.domain;

import com.kimsy.community_service.like.domain.Likes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Long accountId;

    @OneToMany(mappedBy = "member")
    private final List<Likes> likes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Quit quit;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Member(final String nickname, final AccountType accountType, final Long accountId,
            final Quit quit) {
        this.nickname = nickname;
        this.accountType = accountType;
        this.accountId = accountId;
        this.quit = quit;
    }

    public void validateAccountType(final AccountType accountType) {
        if (this.accountType != accountType) {
            throw new IllegalArgumentException("해당 회원의 AccountType과 일치하지 않습니다.");
        }
    }

    public String getKorAccountType() {
        return accountType.getKorAccountType();
    }

    public void addLikes(final Likes likes) {
        this.likes.add(likes);
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public List<Likes> getLikes() {
        return Collections.unmodifiableList(likes);
    }

    public Quit getQuit() {
        return quit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(nickname,
                member.nickname) && accountType == member.accountType && Objects.equals(
                accountId, member.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, accountType, accountId);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", accountType=" + accountType +
                ", accountId=" + accountId +
                ", quit=" + quit +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
