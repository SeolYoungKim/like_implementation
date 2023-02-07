package com.kimsy.community_service.post.presentation;

import com.kimsy.community_service.post.application.PostService;
import com.kimsy.community_service.post.application.dto.PostDeleteResponse;
import com.kimsy.community_service.post.application.dto.PostResponse;
import com.kimsy.community_service.post.presentation.dto.PostCreateRequest;
import com.kimsy.community_service.post.presentation.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public Page<PostResponse> getPosts(
            @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
        return postService.getPosts(pageable);
    }

    @PostMapping("/posts")
    public PostResponse createPost(@RequestBody PostCreateRequest postCreateRequest,
            Authentication authentication) {
        return postService.createPost(postCreateRequest, authentication);
    }

    @PutMapping("/posts/{postId}")
    public PostResponse updatePost(@PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest, Authentication authentication) {
        return postService.updatePost(postId, postUpdateRequest, authentication);
    }

    @DeleteMapping("/posts/{postId}")
    public PostDeleteResponse deletePost(@PathVariable Long postId, Authentication authentication) {
        return postService.deletePost(postId, authentication);
    }
}
