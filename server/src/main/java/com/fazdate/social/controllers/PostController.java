package com.fazdate.social.controllers;

import com.fazdate.social.models.Post;
import com.fazdate.social.services.modelServices.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PutMapping("/createPost")
    public void createPost(@RequestBody Post post) throws ExecutionException, InterruptedException {
        service.createPost(post);
    }

    @GetMapping("/getPost")
    public Post getPost(@RequestParam String userId, @RequestParam String postId) throws InterruptedException, ExecutionException {
        return service.getPost(userId, postId);
    }

    @PutMapping("/deletePost")
    public void deletePost(@RequestParam String userId, @RequestParam String postId) throws ExecutionException, InterruptedException {
        service.deletePost(userId, postId);
    }

    @PutMapping("/incrementLikeOnPost")
    public void incrementLikeOnPost(@RequestParam String userId, @RequestParam String postId) throws ExecutionException, InterruptedException {
        service.incrementLikeOnPost(userId, postId);
    }
}
