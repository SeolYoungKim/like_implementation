package com.kimsy.community_service.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kimsy.community_service.auth.MockAuthentication;
import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.member.domain.Quit;
import com.kimsy.community_service.post.application.dto.PostDeleteResponse;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostRepository;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import com.kimsy.community_service.post.presentation.dto.PostUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    private Authentication mockAuth;
    private Member member;
    private PostService postService;

    @BeforeEach
    void setUp() {
        mockAuth = new MockAuthentication();
        member = new Member("중개사임", AccountType.REALTOR, 47L, Quit.NO);
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

            final PostResponse postResponse = postService.createPost(dto, mockAuth);
            assertThat(postResponse.getTitle()).isEqualTo(expectedTitle);
            assertThat(postResponse.getContents()).isEqualTo(expectedContents);
        }

        @DisplayName("회원이 아닌 사람이 요청한 경우, 즉 Authentication이 null로 넘어올 경우 예외를 발생시킨다.")
        @Test
        void failByAuthentication() {
            final PostCreateRequest dto = new PostCreateRequest("title", "contents");
            assertThatThrownBy(() -> postService.createPost(dto, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }

        @DisplayName("accountId로 조회되지 않는 회원이 요청한 경우 예외를 발생시킨다.")
        @Test
        void failByAccountId() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.empty());

            final PostCreateRequest dto = new PostCreateRequest("title", "contents");
            assertThatThrownBy(() -> postService.createPost(dto, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 회원입니다.");
        }
    }

    @DisplayName("게시글을 수정할 때")
    @Nested
    class Update {
        private final Long postId = 100L;
        private final String expectedTitle = "제목";
        private final String expectedContents = "내용";

        private Post post;
        private PostUpdateRequest postUpdateRequest;

        @BeforeEach
        void setUp() {
            post = new Post("title", "contents", member);
            postUpdateRequest = new PostUpdateRequest(expectedTitle, expectedContents);
        }

        @DisplayName("올바른 값이 넘어왔을 경우 게시글이 수정된다.")
        @Test
        void success() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            final PostResponse postResponse = postService.updatePost(postId, postUpdateRequest,
                    mockAuth);
            assertThat(postResponse.getTitle()).isEqualTo(expectedTitle);
            assertThat(postResponse.getContents()).isEqualTo(expectedContents);
        }

        @DisplayName("없는 게시글일 경우 예외를 발생시킨다.")
        @Test
        void failByNotExistPost() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.updatePost(postId, postUpdateRequest, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 게시글입니다.");
        }

        @DisplayName("회원이 아닌 사람이 요청한 경우, 즉 Authentication이 null로 넘어올 경우 예외를 발생시킨다.")
        @Test
        void failByAuthentication() {
            assertThatThrownBy(() -> postService.updatePost(postId, postUpdateRequest, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }

        @DisplayName("accountId로 조회되지 않는 회원이 요청한 경우 예외를 발생시킨다.")
        @Test
        void failByAccountId() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.updatePost(postId, postUpdateRequest, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 회원입니다.");
        }

        @DisplayName("게시글 작성자가 아닌 경우 예외를 발생시킨다.")
        @Test
        void failByNotAuthor() {
            final Member notAuthor = new Member("notAuthor", AccountType.REALTOR, 34L, Quit.NO);

            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(notAuthor));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            assertThatThrownBy(() -> postService.updatePost(postId, postUpdateRequest, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성자가 아니면 수정/삭제 요청을 할 수 없습니다.");
        }
    }

    @DisplayName("게시글을 삭제할 때")
    @Nested
    class Delete {
        private final Long postId = 100L;
        private Post post;

        @BeforeEach
        void setUp() {
            post = new Post("title", "contents", member);
        }

        @DisplayName("올바른 값이 넘어왔을 경우 게시글이 삭제된다.")
        @Test
        void success() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            final PostDeleteResponse postResponse = postService.deletePost(postId, mockAuth);
            assertThat(postResponse.isDeleted()).isTrue();
        }

        @DisplayName("없는 게시글일 경우 예외를 발생시킨다.")
        @Test
        void failByNotExistPost() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePost(postId, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 게시글입니다.");
        }

        @DisplayName("회원이 아닌 사람이 요청한 경우, 즉 Authentication이 null로 넘어올 경우 예외를 발생시킨다.")
        @Test
        void failByAuthentication() {
            assertThatThrownBy(() -> postService.deletePost(postId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }

        @DisplayName("accountId로 조회되지 않는 회원이 요청한 경우 예외를 발생시킨다.")
        @Test
        void failByAccountId() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePost(postId, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 회원입니다.");
        }

        @DisplayName("게시글 작성자가 아닌 경우 예외를 발생시킨다.")
        @Test
        void failByNotAuthor() {
            final Member notAuthor = new Member("notAuthor", AccountType.REALTOR, 34L, Quit.NO);

            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(notAuthor));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            assertThatThrownBy(() -> postService.deletePost(postId, mockAuth))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성자가 아니면 수정/삭제 요청을 할 수 없습니다.");
        }
    }

    @DisplayName("게시글을 조회할 때")
    @Nested
    class Read {
        private final Member member = new Member("중개사", AccountType.REALTOR, 47L, Quit.NO);

        @DisplayName("단건 조회 요청을 할 경우, 단건의 게시글이 조회된다.")
        @Test
        void getPost() {
            final String expectedTitle = "title";
            final String expectedContents = "contents";
            final Post post = new Post(expectedTitle, expectedContents, member);
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            final Long anyPostId = 100L;
            final PostResponse postResponse = postService.getPost(anyPostId);

            assertThat(postResponse.getTitle()).isEqualTo(expectedTitle);
            assertThat(postResponse.getContents()).isEqualTo(expectedContents);
        }

        @DisplayName("여러건 조회를 할 경우, 페이징이 적용된 결과가 반환된다.")
        @Test
        void getPosts() {
            final List<Post> posts = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> new Post("title" + i, "contents" + i, member))
                    .collect(Collectors.toList());

            final Pageable pageable = PageRequest.of(0, 10);
            final PageImpl<Post> page = new PageImpl<>(posts, pageable, 10);
            when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

            final Page<PostResponse> postResponses = postService.getPosts(pageable);
            assertThat(postResponses.getTotalPages()).isEqualTo(1);
            assertThat(postResponses.getTotalElements()).isEqualTo(10);
        }
    }
}