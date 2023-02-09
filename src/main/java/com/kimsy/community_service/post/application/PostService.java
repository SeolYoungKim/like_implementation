package com.kimsy.community_service.post.application;

import com.kimsy.community_service.auth.CustomAuthentication;
import com.kimsy.community_service.like.domain.Likes;
import com.kimsy.community_service.like.domain.LikesRepository;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.post.application.dto.PostDeleteResponse;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.application.dto.PostsPageResponse;
import com.kimsy.community_service.post.domain.Delete;
import com.kimsy.community_service.post.domain.Post;
import com.kimsy.community_service.post.domain.PostQueryRepository;
import com.kimsy.community_service.post.domain.PostRepository;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import com.kimsy.community_service.post.presentation.dto.PostUpdateRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    public PostService(
            final PostRepository postRepository,
            final PostQueryRepository postQueryRepository,
            final MemberRepository memberRepository,
            final LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.postQueryRepository = postQueryRepository;
        this.memberRepository = memberRepository;
        this.likesRepository = likesRepository;
    }

    public PostResponse createPost(final PostCreateRequest postCreateRequest,
            final Authentication authentication) {
        validateAuthenticationIsNull(authentication);

        final Member member = getMemberBy(authentication);
        final Post post = createPostBy(postCreateRequest, member);

        postRepository.save(post);

        return PostResponse.from(post);
    }

    private Post createPostBy(final PostCreateRequest postCreateRequest, final Member member) {
        return new Post(postCreateRequest.getTitle(), postCreateRequest.getContents(), member,
                Delete.NO);
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

    public PostDeleteResponse deletePost(final Long postId, final Authentication authentication) {
        validateAuthenticationIsNull(authentication);

        final Member member = getMemberBy(authentication);
        final Post post = getPostBy(postId);

        post.validateAuthor(member);
        post.delete();

        return new PostDeleteResponse(true);
    }

    private void validateAuthenticationIsNull(final Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("게시글 작성/수정/삭제는 회원만 할 수 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Page<PostsPageResponse> getPosts(final Pageable pageable,
            final Authentication authentication) {
        final Page<Post> posts = postQueryRepository.getPosts(pageable);
        if (authentication == null) {
            return posts.map(PostsPageResponse::from);
        }

        final Member member = getMemberBy(authentication);
        return posts.map(post ->
                generateDto(member, post, PostsPageResponse::from, PostsPageResponse::from));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(final Long postId, final Authentication authentication) {
        final Post post = getPostBy(postId);
        if (authentication == null) {
            return PostResponse.from(post);
        }

        final Member member = getMemberBy(authentication);
        return generateDto(member, post, PostResponse::from, PostResponse::from);
    }

    private Post getPostBy(final Long postId) {
        return postQueryRepository.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글입니다."));
    }

    private Member getMemberBy(final Authentication authentication) {
        final CustomAuthentication auth = (CustomAuthentication) authentication;
        final Member member = memberRepository.findByAccountId(auth.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        member.validateAccountType(auth.getAccountType());

        return member;
    }

    private <T> T generateDto(
            final Member member, final Post post,
            final DtoGeneratorUsingPostAndLikeStatus<T> dtoGeneratorUsingPostAndLikeStatus,
            final DtoGeneratorUsingOnlyPost<T> dtoGeneratorUsingOnlyPost
    ) {
        final Optional<Likes> likes = likesRepository.findByMemberAndPost(member, post);
        if (likes.isPresent()) {
            return dtoGeneratorUsingPostAndLikeStatus.generate(post, likes.get().getLikeStatus());
        }

        return dtoGeneratorUsingOnlyPost.generate(post);
    }
}
