package com.kimsy.community_service.post.application;

import com.kimsy.community_service.auth.CustomAuthentication;
import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostRepository;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import com.kimsy.community_service.post.presentation.dto.PostUpdateRequest;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
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
            throw new IllegalArgumentException("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }
    }

    private Member getMemberBy(final Authentication authentication) {
        final CustomAuthentication auth = (CustomAuthentication) authentication;
        final Member member = memberRepository.findByAccountId(auth.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        member.validateAccountType(auth.getAccountType());
        return member;
    }

    private Post createPostBy(final PostCreateRequest postCreateRequest, final Member member) {
        return new Post(postCreateRequest.getTitle(), postCreateRequest.getContents(), member);
    }

    public PostResponse updatePost(final Long postId, final PostUpdateRequest postUpdateRequest,
            final Authentication authentication) {
        validateAuthenticationIsNull(authentication);

        final Member member = getMemberBy(authentication);
        final Post post = getPostBy(postId);

        post.validateAuthor(member);
        post.update(postUpdateRequest.getTitle(), postUpdateRequest.getContents());

        return PostResponse.from(post);
    }

    private Post getPostBy(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글입니다."));
    }


    // 테스트용 TODO 추후 삭제
    @PostConstruct
    public void initData() {
        final List<Member> members = Arrays.asList(
                new Member("중개사임", AccountType.REALTOR, 47L),
                new Member("갓물주", AccountType.LESSOR, 21L),
                new Member("월세좀싸게해주세요", AccountType.LESSEE, 562L));

        memberRepository.saveAll(members);

        final List<Post> posts = Arrays.asList(
                new Post("중개사 너무 힘들다", "돈좀 많이벌고싶다.", members.get(0)),
                new Post("갓물주도 힘들다 ㅠ", "세금좀 내려주라..", members.get(1)),
                new Post("배부른 소리들 하네", "전세 사기나 치지마.. 월세도 개비쌈;", members.get(2)));

        postRepository.saveAll(posts);
    }

}
