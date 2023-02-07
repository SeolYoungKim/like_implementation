package com.kimsy.community_service.post.application;

import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostRepository;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostResponse createPost(final PostCreateRequest postCreateRequest,
            final Authentication authentication) {
        validateAuthenticationIsNull(authentication);

        final Member member = getMemberBy(authentication);
        final Post post = createPostBy(postCreateRequest, member);
        postRepository.save(post);

        return PostResponse.from(post);
    }

    private void validateAuthenticationIsNull(final Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("글 작성은 회원만 할 수 있습니다.");
        }
    }

    private Member getMemberBy(final Authentication authentication) {
        final Long memberId = (Long) authentication.getPrincipal();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
    }

    private static Post createPostBy(final PostCreateRequest postCreateRequest, final Member member) {
        return new Post(postCreateRequest.getTitle(), postCreateRequest.getContents(),
                member);
    }
}
