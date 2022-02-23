package com.fazdate.social.services.modelServices;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.google.cloud.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {
    private final FirestoreService service;
    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    public PostService (FirestoreService service) {
        this.service = service;
    }

    public void createPost(Post post) throws ExecutionException, InterruptedException {
        User user = service.getObjectFromDocument(Names.USERS, User.class, post.getPosterUserId());
        post.setTimestamp(Timestamp.now());
        user.getPosts().add(post);
        service.updateDocumentInCollection(Names.USERS, user, user.getUserId());
        LOGGER.info("User with userId " + user.getUserId() + " has a new post!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public void deletePost(String userId, String postId) throws ExecutionException, InterruptedException {
        User user = service.getObjectFromDocument(Names.USERS, User.class, userId);
        int postIndex = getPostIndexFromPostId(user, postId);
        List<Post> posts = user.getPosts();
        posts.remove(postIndex);
        user.setPosts(posts);
        service.updateDocumentInCollection(Names.USERS, user, user.getUserId());
        LOGGER.info("Post with postId " + postId + " was deleted!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    private int getPostIndexFromPostId(User user, String postId) {
        int i = 0;
        for (Post post : user.getPosts()) {
            if (post.getPostId().equals(postId)) {
                return i;
            }
            i++;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This post doesn't exists");
    }

    public Post getPost(String userId, String postId) throws ExecutionException, InterruptedException {
        List<Post> posts = service.getObjectFromDocument(Names.USERS, User.class, userId).getPosts();
        for (Post post : posts) {
            if (post.getPostId().equals(postId)) {
                return post;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This post doesn't exists");
    }

    public void incrementLikeOnPost(String userId, String postId) throws ExecutionException, InterruptedException {
        User user = service.getObjectFromDocument(Names.USERS, User.class, userId);
        int postIndex = getPostIndexFromPostId(user, postId);
        List<Post> posts = user.getPosts();
        posts.get(postIndex).setNumberOfLikes(posts.get(postIndex).getNumberOfLikes() + 1);
        user.setPosts(posts);
        service.updateDocumentInCollection(Names.USERS, user, user.getUserId());
        LOGGER.info("Post with postId " + postId + " had its like counter incremented!");
        throw new ResponseStatusException(HttpStatus.OK);
    }
}
