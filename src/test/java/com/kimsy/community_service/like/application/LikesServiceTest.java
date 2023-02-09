package com.kimsy.community_service.like.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kimsy.community_service.auth.MockAuthentication;
import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.like.domain.Likes;
import com.kimsy.community_service.like.domain.LikesRepository;
import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.member.domain.Quit;
import com.kimsy.community_service.post.domain.Delete;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {
    @Mock
    private LikesRepository likesRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;

    private final MockAuthentication mockAuthentication = new MockAuthentication();
    private final Member member = new Member("공인중개사", AccountType.REALTOR, 47L, Quit.NO);
    private final Post post = new Post("title", "contents", member, Delete.NO);
    private final long anyPostId = 100L;

    private LikesService likesService;

    @BeforeEach
    void setUp() {
        likesService = new LikesService(likesRepository, memberRepository, postRepository);
    }

    @DisplayName("좋아요 생성 요청이 들어왔을 때")
    @Nested
    class CreateLikes {
        @DisplayName("올바른 값이 전달될 경우 예외가 발생하지 않는다.")
        @Test
        void success() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
            when(likesRepository.existsByMemberAndPost(any(Member.class),
                    any(Post.class))).thenReturn(false);

            likesService.likePost(anyPostId, mockAuthentication);
        }

        @DisplayName("회원이 아닌 사람이 요청한 경우, 즉 Authentication이 null로 넘어올 경우 예외를 발생시킨다.")
        @Test
        void failByNotMember() {
            assertThatThrownBy(() -> likesService.likePost(anyPostId, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }

        @DisplayName("없는 게시글일 경우 예외를 발생시킨다.")
        @Test
        void failByNotFoundPost() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> likesService.likePost(anyPostId, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 게시글입니다.");
        }

        @DisplayName("없는 회원일 경우 예외를 발생시킨다.")
        @Test
        void failByNotFoundMember() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> likesService.likePost(anyPostId, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는 회원입니다.");
        }

        @DisplayName("이미 좋아요를 눌렀을 경우, 예외를 발생시킨다.")
        @Test
        void failByDuplicatedLikes() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
            when(likesRepository.existsByMemberAndPost(any(Member.class),
                    any(Post.class))).thenReturn(true);

            assertThatThrownBy(() -> likesService.likePost(anyPostId, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("게시글 당 좋아요는 한 번만 누를 수 있습니다.");
        }
    }

    @DisplayName("좋아요 취소 요청이 들어왔을 때")
    @Nested
    class LikesToDislike {
        @DisplayName("좋아요가 LIKE 상태인 경우(좋아요가 눌러져 있는 경우) DISLIKE 상태로 변경되고 예외가 발생하지 않는다.")
        @Test
        void success() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            final Likes likes = new Likes(member, post, LikeStatus.LIKE);
            when(likesRepository.findByMemberAndPost(any(Member.class), any(Post.class)))
                    .thenReturn(Optional.of(likes));

            likesService.dislikePost(anyPostId, mockAuthentication);
            assertThat(likes.getLikeStatus()).isEqualTo(LikeStatus.DISLIKE);
        }

        @DisplayName("좋아요가 DISLIKE 상태인 경우 예외를 발생시킨다.")
        @Test
        void failByAlreadyDisliked() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

            final Likes likes = new Likes(member, post, LikeStatus.DISLIKE);
            when(likesRepository.findByMemberAndPost(any(Member.class), any(Post.class)))
                    .thenReturn(Optional.of(likes));

            assertThatThrownBy(() -> likesService.dislikePost(anyPostId, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 좋아요를 취소한 게시글입니다.");
        }

        @DisplayName("좋아요를 누른적도 없는 경우 예외를 발생시킨다.")
        @Test
        void failByNotFoundLikes() {
            when(memberRepository.findByAccountId(any(Long.class))).thenReturn(Optional.of(member));
            when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
            when(likesRepository.findByMemberAndPost(any(Member.class), any(Post.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> likesService.dislikePost(anyPostId, mockAuthentication))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("누르지 않은 좋아요는 취소할 수 없습니다.");
        }
    }
}