package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Comment;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class CommentGenerator {
    private final CommonDataGenerator commonDataGenerator;

    /**
     * Generates a random comment from a given user to a given post
     */
    public Comment generateComment(String commenterUsername, String postId) throws ExecutionException, InterruptedException {
        Comment comment = Comment.builder()
                .commentId(commonDataGenerator.generateId(Names.COMMENTS))
                .commenterUsername(commenterUsername)
                .timestamp(Timestamp.now().toDate())
                .postId(postId)
                .text(commonDataGenerator.generateText(1, 25))
                .build();
        return comment;
    }
}
