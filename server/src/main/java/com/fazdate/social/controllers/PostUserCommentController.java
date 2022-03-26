package com.fazdate.social.controllers;

import com.fazdate.social.dtos.PostUserCommentDto;
import com.fazdate.social.services.dtoServices.PostUserCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class PostUserCommentController {
    private final PostUserCommentService service;

    @GetMapping("/getPostUserComment")
    public PostUserCommentDto getPostUserComment(@RequestParam String postId) throws InterruptedException, ExecutionException {
        return service.getPostUserComment(postId);
    }

    @GetMapping("/getOwnPostUserComments")
    public ArrayList<PostUserCommentDto> getOwnPostUserComments(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getOwnPostUserComments(username);
    }

    @GetMapping("/getFollowedUsersPostUserComments")
    public ArrayList<PostUserCommentDto> getFollowedUsersPostUserComments(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getFollowedUsersPostUserComments(username);
    }

    @GetMapping("/getOwnAndFollowedUsersPostUserComments")
    public ArrayList<PostUserCommentDto> getOwnAndFollowedUsersPostUserComments(@RequestParam String username) throws ExecutionException, InterruptedException {
        return service.getOwnAndFollowedUsersPostUserComments(username);
    }
}
