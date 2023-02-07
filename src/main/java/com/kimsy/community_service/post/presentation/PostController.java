package com.kimsy.community_service.post.presentation;

import com.kimsy.community_service.post.application.PostService;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public PostResponse createPost(@RequestBody PostCreateRequest postCreateRequest,
            Authentication authentication) {
        return postService.createPost(postCreateRequest, authentication);
    }
}
