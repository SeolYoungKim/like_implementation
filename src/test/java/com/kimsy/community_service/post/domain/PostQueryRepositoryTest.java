package com.kimsy.community_service.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kimsy.community_service.like.domain.LikesRepository;
import com.kimsy.community_service.member.domain.AccountType;
import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.member.domain.MemberRepository;
import com.kimsy.community_service.member.domain.Quit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PostQueryRepositoryTest {
    private static final int TOTAL_POSTS_COUNT = 20;

    @Autowired
    private PostQueryRepository postQueryRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LikesRepository likesRepository;

    @BeforeEach
    void setUp() {
        likesRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

        final Member member = new Member("중개사", AccountType.REALTOR, 47L, Quit.NO);
        memberRepository.save(member);

        final List<Post> posts = IntStream.rangeClosed(1, TOTAL_POSTS_COUNT)
                .mapToObj(i -> new Post("title" + i, "contents" + i, member, Delete.NO))
                .collect(Collectors.toList());
        postRepository.saveAll(posts);
        postRepository.save(new Post("title", "contents", member, Delete.YES));
    }

    @DisplayName("게시글을 여러건 조회할 때")
    @Nested
    class GetPosts {
        @DisplayName("Delete 상태가 NO인 게시글만 조회된다.")
        @Test
        void onlyGetPostsDeleteNo() {
            final Pageable pageable = PageRequest.of(0, TOTAL_POSTS_COUNT + 1);
            final Page<Post> posts = postQueryRepository.getPosts(pageable);

            final long count = posts.stream()
                    .filter(post -> post.getDelete() == Delete.NO)
                    .count();

            assertThat(count).isEqualTo(TOTAL_POSTS_COUNT);
        }

        @DisplayName("생성 날짜 내림차순으로 페이징이 적용된다.")
        @Test
        void pagingByCreatedAt() {
            final Pageable pageable = PageRequest.of(0, 10);
            final Page<Post> page = postQueryRepository.getPosts(pageable);
            final List<Post> posts = page.toList();

            final int firstIdx = 0;
            final int lastIdx = 9;
            assertThat(posts.get(firstIdx).getTitle()).isEqualTo("title20");
            assertThat(posts.get(lastIdx).getTitle()).isEqualTo("title11");
        }
    }
}