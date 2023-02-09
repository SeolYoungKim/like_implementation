package com.kimsy.community_service.like.presentation;

import com.kimsy.community_service.like.application.LikesService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/posts/{postId}")
@RestController
public class LikesController {
    private final LikesService likesService;

    public LikesController(final LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/likes")
    public void likePost(@PathVariable final Long postId, Authentication authentication) {
        likesService.likePost(postId, authentication);
    }

    @DeleteMapping("/likes")
    public void dislikePost(@PathVariable final Long postId, Authentication authentication) {
        likesService.dislikePost(postId, authentication);
    }
}
