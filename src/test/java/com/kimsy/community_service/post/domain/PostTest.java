package com.kimsy.community_service.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostTest {
    private static final Member member = new Member("nickName", AccountType.LESSEE, 512L);

    @DisplayName("객체를 생성할 때")
    @Nested
    class Construct {
        @DisplayName("올바른 값이 넘어오는 경우 예외가 발생하지 않는다.")
        @Test
        void success() {
            new Post("title", "contents", member);
        }

        @DisplayName("제목이나 내용이 넘어오지 않는 경우 예외가 발생한다.")
        @ParameterizedTest
        @CsvSource({",contents", "title,", ",", "'',''", "'',contents", "title,''"})
        void fail(String title, String contents) {
            assertThatThrownBy(() -> new Post(title, contents, member))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제목이나 내용은 빈 값일 수 없습니다.");
        }
    }

    @DisplayName("객체를 수정할 때")
    @Nested
    class Update {
        private final Post post = new Post("title", "contents", member);

        @DisplayName("올바른 값이 넘어오는 경우 예외가 발생하지 않고, Post의 제목과 내용이 수정된다.")
        @Test
        void success() {
            final String expectedTitle = "제목";
            final String expectedContents = "내용";
            post.update(expectedTitle, expectedContents);

            assertThat(post.getTitle()).isEqualTo(expectedTitle);
            assertThat(post.getContents()).isEqualTo(expectedContents);
        }

        @DisplayName("제목이나 내용이 넘어오지 않는 경우 예외가 발생한다.")
        @ParameterizedTest
        @CsvSource({",contents", "title,", ",", "'',''", "'',contents", "title,''"})
        void fail(String title, String contents) {
            assertThatThrownBy(() -> post.update(title, contents))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제목이나 내용은 빈 값일 수 없습니다.");
        }
    }

    @DisplayName("게시글의 저자인지 확인할 때")
    @Nested
    class ValidateAuthor {
        private final Post post = new Post("title", "contents", member);

        @DisplayName("게시글의 저자일 경우 예외가 발생하지 않는다.")
        @Test
        void success() {
            post.validateAuthor(member);
        }

        @DisplayName("게시글의 저자가 아닐 경우 예외를 발생시킨다.")
        @Test
        void fail() {
            final Member notAuthor = new Member("임대인", AccountType.LESSOR, 34L);
            assertThatThrownBy(() -> post.validateAuthor(notAuthor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성자가 아니면 수정/삭제 요청을 할 수 없습니다.");
        }
    }
}