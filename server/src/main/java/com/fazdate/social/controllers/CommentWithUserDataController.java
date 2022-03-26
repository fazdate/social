package com.fazdate.social.controllers;

import com.fazdate.social.dtos.CommentWithUserDataDto;
import com.fazdate.social.services.dtoServices.CommentWithUserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class CommentWithUserDataController {
    private final CommentWithUserDataService commentWithUserDataService;

    @GetMapping("/getCommentWithUserData")
    public CommentWithUserDataDto getCommentWithUserData(String commentId) throws ExecutionException, InterruptedException {
        return commentWithUserDataService.getCommentWithUserData(commentId);
    }
}
