package com.kimsy.community_service.post.domain;

import static com.kimsy.community_service.post.domain.QPost.post;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<Post> getPosts(final Pageable pageable) {
        final List<Post> posts = jpaQueryFactory.selectFrom(post)
                .where(post.delete.eq(Delete.NO))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final JPAQuery<Long> countQuery = jpaQueryFactory.select(post.count()).from(post)
                .where(post.delete.eq(Delete.NO));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    public Optional<Post> getPostById(final Long postId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(post)
                        .where(post.id.eq(postId).and(post.delete.eq(Delete.NO)))
                        .fetchOne());
    }
}
