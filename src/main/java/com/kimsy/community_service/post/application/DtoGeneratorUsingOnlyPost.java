package com.kimsy.community_service.post.application;

import com.kimsy.community_service.post.domain.Post;

@FunctionalInterface
interface DtoGeneratorUsingOnlyPost<T> {
    T generate(final Post post);
}
