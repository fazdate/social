package com.fazdate.social.controllers;

import com.fazdate.social.models.Post;
import com.fazdate.social.services.modelServices.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/likeOrUnlikePost")
    public void likeOrUnlikePost(@RequestParam String postId, @RequestParam String username) throws ExecutionException, InterruptedException {
        service.likeOrUnlikePost(postId, username);
    }


}
