package com.kimsy.community_service.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kimsy.community_service.auth.MockAuthentication;
import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.domain.PostRepository;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    private Authentication mockAuthentication;
    private Member member;
    private PostService postService;

    @BeforeEach
    void setUp() {
        mockAuthentication = new MockAuthentication();
        member = new Member("중개사임", AccountType.REALTOR, 47L);
        postService = new PostService(postRepository, memberRepository);
    }

    @DisplayName("게시글을 생성할 때")
    @Nested
    class Create {
        @DisplayName("올바른 값이 넘어왔을 경우 게시글이 생성된다.")
        @Test
        void success() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));

            final String expectedTitle = "title";
            final String expectedContents = "contents";
            final PostCreateRequest dto = new PostCreateRequest(expectedTitle, expectedContents);

            final PostResponse postResponse = postService.createPost(dto, mockAuthentication);
            assertThat(postResponse.getTitle()).isEqualTo(expectedTitle);
            assertThat(postResponse.getContents()).isEqualTo(expectedContents);
        }

        @DisplayName("회원이 아닌 사람이 요청한 경우, 즉 Authentication이 null로 넘어올 경우 예외를 발생시킨다.")
        @Test
        void failByAuthentication() {
            final PostCreateRequest dto = new PostCreateRequest("title", "contents");
            assertThatThrownBy(() -> postService.createPost(dto, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("글 작성은 회원만 할 수 있습니다.");
        }

        @DisplayName("accountId로 조회되지 않는 회원이 요청한 경우 예외를 발생시킨다.")
        @Test
        void failByAccountId() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.empty());
            final PostCreateRequest dto = new PostCreateRequest("title", "contents");
            assertThatThrownBy(() -> postService.createPost(dto, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 회원입니다.");
        }
    }
}