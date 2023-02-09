package com.kimsy.community_service.post.application;

import com.kimsy.community_service.like.domain.LikeStatus;
import com.kimsy.community_service.post.domain.Post;

@FunctionalInterface
interface DtoGeneratorUsingPostAndLikeStatus<T> {
    T generate(final Post post, final LikeStatus likeStatus);
}
