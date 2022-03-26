package com.fazdate.social.services.modelServices;

import com.fazdate.social.generators.CommonDataGenerator;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FirestoreService service;
    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
    private final CommonDataGenerator commonDataGenerator;

    /**
     * Creates a post
     */
    public void createPost(Post post) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(post.getPosterUsername());
        user.getPosts().add(post.getPostId());
        service.updateDocumentInCollection(Names.USERS, user, user.getUsername());
        service.addDocumentToCollection(Names.POSTS, post, post.getPostId());
        LOGGER.info(user.getUsername() + " has a new post!");
    }

    /**
     * Returns a unique random id for a new Post
     */
    public String generatePostId() throws ExecutionException, InterruptedException {
        return commonDataGenerator.generateId(Names.POSTS);
    }

    /**
     * Updates a post
     */
    public void updatePost(Post post) {
        updatePostPrivate(post);
        LOGGER.info("Post with postId: " + post.getPostId() + " was updated!");
    }

    /**
     * Deletes a post
     */
    public void deletePost(String postId) throws ExecutionException, InterruptedException {
        Post post = getPost(postId);
        User user = getUserFromUsername(post.getPosterUsername());
        int postIndex = getPostIndexFromPostId(user, postId);
        user.getPosts().remove(postIndex);
        service.updateDocumentInCollection(Names.USERS, user, user.getUsername());
        service.deleteDocumentFromCollection(Names.POSTS, postId);
        LOGGER.info("Post with postId " + postId + " was deleted!");
    }

    private int getPostIndexFromPostId(User user, String postId) {
        int i = 0;
        for (String post : user.getPosts()) {
            if (post.equals(postId)) {
                return i;
            }
            i++;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This post doesn't exists");
    }

    /**
     * Returns a post of the given id
     */
    public Post getPost(String postId) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.POSTS, Post.class, postId);
    }

    /**
     * Likes or unlikes a post, depending on if the given user has already liked the given post or not.
     */
    public void likeOrUnlikePost(String postId, String username) throws ExecutionException, InterruptedException {
        Post post = getPost(postId);
        if (post.getUsersThatLiked().contains((username))) {
            post.getUsersThatLiked().remove(username);
            LOGGER.info(username + " unliked the post with the id " + postId);
        } else {
            post.getUsersThatLiked().add(username);
            LOGGER.info(username + " liked the post with the id " + postId);
        }
        updatePostPrivate(post);
    }

    private User getUserFromUsername(String username) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.USERS, User.class, username);
    }

    private void updatePostPrivate(Post post) {
        service.updateDocumentInCollection(Names.POSTS, post, post.getPostId());
    }

}
