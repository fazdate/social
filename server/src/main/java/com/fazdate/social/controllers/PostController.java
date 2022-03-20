package com.fazdate.social.controllers;

import com.fazdate.social.models.Post;
import com.fazdate.social.services.modelServices.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping("/createPost")
    public void createPost(@RequestBody Post post) throws ExecutionException, InterruptedException {
        service.createPost(post);
    }

    @GetMapping("/generatePostId")
    public String generatePostId() throws ExecutionException, InterruptedException {
        return service.generatePostId();
    }

    @GetMapping("/getPost")
    public Post getPost(@RequestParam String postId) throws InterruptedException, ExecutionException {
        return service.getPost(postId);
    }

    @GetMapping("/getOwnPosts")
    public ArrayList<Post> getOwnPosts(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getOwnPosts(username);
    }

    @GetMapping("/getFollowedUsersPosts")
    public ArrayList<Post> getFollowedUsersPosts(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getFollowedUsersPosts(username);
    }

    @GetMapping("/getOwnAndFollowedUsersPosts")
    public ArrayList<Post> getOwnAndFollowedUsersPosts(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getOwnAndFollowedUsersPosts(username);
    }

    @PutMapping("/deletePost")
    public void deletePost(@RequestParam String postId) throws ExecutionException, InterruptedException {
        service.deletePost(postId);
    }

    @PutMapping("/incrementLikeOnPost")
    public void incrementLikeOnPost(@RequestParam String userId, @RequestParam String postId) throws ExecutionException, InterruptedException {
        service.incrementLikeOnPost(userId, postId);
    }
}
