package com.kimsy.community_service.test_data;

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
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * 테스트용 Data 입니다.
 */

@Component
public class TestData {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    public TestData(final PostRepository postRepository, final MemberRepository memberRepository,
            final LikesRepository likesRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.likesRepository = likesRepository;
    }

    @PostConstruct
    public void initData() {
        final List<Member> members = Arrays.asList(
                new Member("중개사임", AccountType.REALTOR, 1L, Quit.NO),
                new Member("갓물주", AccountType.LESSOR, 2L, Quit.NO),
                new Member("아기개발자", AccountType.LESSEE, 3L, Quit.NO));

        memberRepository.saveAll(members);

        final Member realtor = members.get(0);
        final Member lessor = members.get(1);
        final Member lessee = members.get(2);

        final List<Post> posts = Arrays.asList(
                new Post("중개사 너무 힘들다", "돈좀 많이벌고싶다.", realtor, Delete.NO),
                new Post("갓물주도 힘들다 ㅠ", "세금좀 내려주라..", lessor, Delete.NO),
                new Post("응애", "월세 좀만 싸게 해주세요", lessee, Delete.NO));

        postRepository.saveAll(posts);

        final Post firstPost = posts.get(0);
        final Post secondPost = posts.get(1);
        final Post thirdPost = posts.get(2);

        final List<Likes> likes = Arrays.asList(
                new Likes(realtor, firstPost, LikeStatus.LIKE),
                new Likes(realtor, secondPost, LikeStatus.LIKE),
                new Likes(realtor, thirdPost, LikeStatus.LIKE),

                new Likes(lessor, firstPost, LikeStatus.LIKE),
                new Likes(lessor, secondPost, LikeStatus.LIKE),
                new Likes(lessor, thirdPost, LikeStatus.DISLIKE),

                new Likes(lessee, secondPost, LikeStatus.DISLIKE),
                new Likes(lessee, thirdPost, LikeStatus.LIKE));

        likesRepository.saveAll(likes);
    }
}
