package com.fazdate.social.services.modelServices;

import com.fazdate.social.generators.CommonDataGenerator;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FirestoreService service;
    private final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
    private final CommonDataGenerator commonDataGenerator;

    public void createPost(Post post) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(post.getPosterUsername());
        user.getPosts().add(post.getPostId());
        service.updateDocumentInCollection(Names.USERS, user, user.getUsername());
        service.addDocumentToCollection(Names.POSTS, post, post.getPostId());
        LOGGER.info(user.getUsername() + " has a new post!");
    }

    public String generatePostId() throws ExecutionException, InterruptedException {
        return commonDataGenerator.generateId(Names.POSTS);

    }

    public void updatePost(Post post) {
        updatePostPrivate(post);
        LOGGER.info("Post with postId: " + post.getPostId() + " was updated!");
    }

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

    public Post getPost(String postId) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.POSTS, Post.class, postId);
    }

    public void incrementLikeOnPost(String userId, String postId) throws ExecutionException, InterruptedException {
        Post post = getPost(postId);
        post.getUsersThatLiked().add(userId);
        updatePostPrivate(post);
        LOGGER.info("Post with postId " + postId + " had its like counter incremented!");
    }

    public ArrayList<Post> getOwnPosts(String username) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(username);
        ArrayList<Post> ownPosts = new ArrayList<>();
        for (String postId : user.getPosts()) {
            ownPosts.add(getPost(postId));
        }
        ownPosts.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return ownPosts;
    }

    public ArrayList<Post> getFollowedUsersPosts(String username) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(username);
        ArrayList<Post> followedUsersPost = new ArrayList<>();
        for (String followedUser : user.getFollowedUsers()) {
            User followedUsr = getUserFromUsername(followedUser);
            for (String postId : followedUsr.getPosts()) {
                followedUsersPost.add(getPost(postId));
            }
        }
        followedUsersPost.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return followedUsersPost;
    }

    public ArrayList<Post> getOwnAndFollowedUsersPosts(String username) throws ExecutionException, InterruptedException {
        ArrayList<Post> ownPosts = getOwnPosts(username);
        ArrayList<Post> followedUsersPosts = getFollowedUsersPosts(username);
        ArrayList<Post> ownAndFollowedUsersPosts = new ArrayList<>();
        ownAndFollowedUsersPosts.addAll(ownPosts);
        ownAndFollowedUsersPosts.addAll(followedUsersPosts);
        ownAndFollowedUsersPosts.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return ownAndFollowedUsersPosts;
    }

    private User getUserFromUsername(String username) throws ExecutionException, InterruptedException {
        return service.getObjectFromDocument(Names.USERS, User.class, username);
    }

    private void updatePostPrivate(Post post) {
        service.updateDocumentInCollection(Names.POSTS, post, post.getPostId());
    }

}
