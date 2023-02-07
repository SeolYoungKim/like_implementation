package com.kimsy.community_service.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("공인중개사", AccountType.REALTOR, 47L, Quit.NO);
    }

    @DisplayName("AccountType이 일치하면 예외가 발생하지 않는다.")
    @Test
    void success() {
        member.validateAccountType(AccountType.REALTOR);
    }

    @DisplayName("AccountType이 일치하지 않으면 예외를 발생시킨다.")
    @Test
    void fail() {
        assertThatThrownBy(() -> member.validateAccountType(AccountType.LESSEE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 회원의 AccountType과 일치하지 않습니다.");
    }
}