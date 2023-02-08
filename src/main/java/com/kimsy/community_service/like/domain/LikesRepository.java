package com.kimsy.community_service.like.domain;

import com.kimsy.community_service.member.domain.Member;
import com.kimsy.community_service.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByMemberAndPost(final Member member, final Post post);
}
