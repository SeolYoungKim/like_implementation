package com.kimsy.community_service.member.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
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
