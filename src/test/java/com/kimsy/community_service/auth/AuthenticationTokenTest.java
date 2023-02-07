package com.kimsy.community_service.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthenticationTokenTest {
    @DisplayName("올바른 형식의 토큰이 전달되면 객체 생성에 성공한다.")
    @ParameterizedTest(name = "입력=\"{0}\"")
    @ValueSource(strings = {"Realtor 47", "Realtor 47 ", " Realtor 47"})
    void successConstruct(String correctToken) {
        new AuthenticationToken(correctToken);
    }

    @DisplayName("올바르지 않은 형식의 토큰이 전달되는 경우")
    @Nested
    class failConstruct {
        @DisplayName("토큰 포맷(ex: Realtor 412)이 지켜지지 않은 경우")
        @Test
        void malformedToken() {
            final String malformedToken = "Realtor 48 85";
            assertThatThrownBy(() -> new AuthenticationToken(malformedToken))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("지원하는 토큰 형식이 아닙니다.");
        }

        @DisplayName("토큰의 두 번째 값인 accountId가 숫자 형식이 아닌 경우")
        @Test
        void accountIdIsNotNumberFormat() {
            final String notNumberFormat = "Realtor 123a";
            assertThatThrownBy(() -> new AuthenticationToken(notNumberFormat))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("accountId는 숫자 형식이어야 합니다.");
        }

        @DisplayName("지원되지 않는 역할인 경우")
        @Test
        void notSupportedRole() {
            final String notSupportedRole = "User 47";
            assertThatThrownBy(() -> new AuthenticationToken(notSupportedRole))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("지원되지 않는 역할입니다");
        }
    }
}