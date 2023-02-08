package com.kimsy.community_service.like.application;

import com.kimsy.community_service.auth.CustomAuthentication;
import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.like.domain.Likes;
import com.kimsy.community_service.like.domain.LikesRepository;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikesService(final LikesRepository likesRepository,
            final MemberRepository memberRepository, final PostRepository postRepository) {
        this.likesRepository = likesRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public void likePost(final Long postId, final Authentication authentication) {
        validateAuthenticationIsNull(authentication);

        final Likes likes = createLikes(postId, authentication);
        likesRepository.save(likes);
    }

    private void validateAuthenticationIsNull(final Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }
    }

    private Likes createLikes(final Long postId, final Authentication authentication) {
        final Member member = getMemberBy(authentication);
        final Post post = getPostBy(postId);
        validateDuplicationOfLikes(member, post);

        return new Likes(post, member, LikeStatus.LIKE);
    }

    private Member getMemberBy(final Authentication authentication) {
        final CustomAuthentication auth = (CustomAuthentication) authentication;
        final Member member = memberRepository.findByAccountId(auth.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        member.validateAccountType(auth.getAccountType());

        return member;
    }

    private Post getPostBy(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글입니다."));
    }

    private void validateDuplicationOfLikes(final Member member, final Post post) {
        if (likesRepository.existsByMemberAndPost(member, post)) {
            throw new IllegalArgumentException("게시글 당 좋아요는 한 번만 누를 수 있습니다.");
        }
    }
}
