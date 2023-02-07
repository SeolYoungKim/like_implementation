package com.kimsy.community_service.post.domain;

import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostTest {
    @DisplayName("객체를 생성할 때")
    @Nested
    class Construct {
        private final Member member = new Member("nickName", AccountType.LESSEE, 512L);

        @DisplayName("올바른 값이 넘어오는 경우 예외가 발생하지 않는다.")
        @Test
        void success() {
            new Post("title", "contents", member);
        }

        @DisplayName("제목이나 내용이 넘어오지 않는 경우 예외가 발생한다.")
        @ParameterizedTest
        @CsvSource({",contents", "title,", ",", "'',''", "'',contents", "title,''"})
        void fail(String title, String contents) {
            Assertions.assertThatThrownBy(() -> new Post(title, contents, member))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제목이나 내용은 빈 값일 수 없습니다.");
        }
    }
}