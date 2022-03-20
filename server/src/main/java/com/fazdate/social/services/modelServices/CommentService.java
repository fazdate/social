package com.fazdate.social.services.modelServices;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Comment;
import com.fazdate.social.models.Post;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final FirestoreService service;
    private final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    public void addComment(Comment comment) throws ExecutionException, InterruptedException {
        Post post = getPost(comment.getPostId());
        post.getCommentIds().add(comment.getCommentId());
        service.addDocumentToCollection(Names.COMMENTS, comment, comment.getCommentId());
        LOGGER.info(comment.getCommenterUsername() + " has commented on the post with id of " + comment.getPostId());
    }

    public Comment getComment(String commentId) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.COMMENTS, Comment.class, commentId);
    }

    public void deleteComment(String commentId) throws ExecutionException, InterruptedException {
        Comment comment = getComment(commentId);
        Post post = getPost(comment.getPostId());
        post.getCommentIds().remove(commentId);
        service.updateDocumentInCollection(Names.POSTS, Post.class, post.getPostId());
        service.deleteDocumentFromCollection(Names.COMMENTS, commentId);
    }

    public ArrayList<Comment> getEveryCommentOfPost(String postId) throws ExecutionException, InterruptedException {
        Post post = getPost(postId);
        ArrayList<Comment> comments = new ArrayList<>();
        for (String commentId: post.getCommentIds()) {
            comments.add(getComment(commentId));
        }
        return comments;
    }

    private Post getPost(String postId) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.POSTS, Post.class, postId);
    }
}
