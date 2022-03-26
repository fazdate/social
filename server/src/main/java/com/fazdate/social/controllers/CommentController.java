package com.fazdate.social.controllers;

import com.fazdate.social.models.Comment;
import com.fazdate.social.services.modelServices.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping("/addComment")
    public void addComment(@RequestBody Comment comment) throws ExecutionException, InterruptedException {
        service.addComment(comment);
    }

    @GetMapping("/getComment")
    public Comment getComment(@RequestParam String commentId) throws ExecutionException, InterruptedException {
        return service.getComment(commentId);
    }

    @GetMapping("/getEveryCommentOfPost")
    public Comment[] getEveryCommentOfPost(@RequestParam String postId) throws ExecutionException, InterruptedException {
        return service.getEveryCommentOfPost(postId);
    }


    @PutMapping("/deleteComment")
    public void addComment(@RequestParam String commentId) throws ExecutionException, InterruptedException {
        service.deleteComment(commentId);
    }

    @GetMapping("/generateCommentId")
    public String generateCommentId() throws ExecutionException, InterruptedException {
        return service.generateCommentId();
    }


}
