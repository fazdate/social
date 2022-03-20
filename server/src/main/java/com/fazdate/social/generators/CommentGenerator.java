package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Comment;

import java.util.concurrent.ExecutionException;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentGenerator {
    private final CommonDataGenerator commonDataGenerator;

    public Comment generateComment(String commentedUsername, String postId) throws ExecutionException, InterruptedException {
        Comment comment = new Comment();
        comment.setCommentId(commonDataGenerator.generateId(Names.COMMENTS));
        comment.setCommenterUsername(commentedUsername);
        comment.setTimestamp(Timestamp.now());
        comment.setPostId(postId);
        comment.setText(commonDataGenerator.generateText(1, 25));
        return comment;
    }
}
