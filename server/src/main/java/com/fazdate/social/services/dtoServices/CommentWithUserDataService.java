package com.fazdate.social.services.dtoServices;

import com.fazdate.social.dtos.CommentWithUserDataDto;
import com.fazdate.social.models.Comment;
import com.fazdate.social.models.User;
import com.fazdate.social.services.modelServices.CommentService;
import com.fazdate.social.services.modelServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CommentWithUserDataService {
    private final CommentService commentService;
    private final UserService userService;

    /**
     * Returns a comment with the commenter's displayName and their photo
     */
    public CommentWithUserDataDto getCommentWithUserData(String commentId) throws ExecutionException, InterruptedException {
        Comment comment = commentService.getComment(commentId);
        User user = userService.getUser(comment.getCommenterUsername());
        return CommentWithUserDataDto.builder()
                .displayName(user.getDisplayName())
                .photoUrl(user.getPhotoURL())
                .comment(comment)
                .build();
    }
}
