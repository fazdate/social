package com.fazdate.social.services.dtoServices;

import com.fazdate.social.dtos.PostUserCommentDto;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Comment;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.fazdate.social.services.modelServices.CommentService;
import com.fazdate.social.services.modelServices.PostService;
import com.fazdate.social.services.modelServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
public class PostUserCommentService {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final FirestoreService firestoreService;

    /**
     * Returns a post, the comments to the post, and the poster's displayName and their photo
     */
    public PostUserCommentDto getPostUserComment(String postId) throws ExecutionException, InterruptedException {
        Post post = postService.getPost(postId);
        User user = userService.getUser(post.getPosterUsername());
        Comment[] comments = commentService.getEveryCommentOfPost(postId);
        return PostUserCommentDto.builder()
                .comments(comments)
                .displayName(user.getDisplayName())
                .photoUrl(user.getPhotoURL())
                .post(post)
                .build();
    }

    /**
     * Returns a list of the posts of the given user with the comments on the post and the user's displayName and their photo in a chronological order
     */
    public ArrayList<PostUserCommentDto> getOwnPostUserComments(String username) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(username);
        ArrayList<PostUserCommentDto> ownPosts = new ArrayList<>();
        for (String postId : user.getPosts()) {
            ownPosts.add(getPostUserComment(postId));
        }
        ownPosts.sort(Comparator.comparing((PostUserCommentDto post) -> post.getPost().getTimestamp()));
        return ownPosts;
    }

    /**
     * Returns a list of every post, the post's comment and the poster's displayName and photoUrl from users the given user follows in a chronological order
     */
    public ArrayList<PostUserCommentDto> getFollowedUsersPostUserComments(String username) throws ExecutionException, InterruptedException {
        User user = getUserFromUsername(username);
        ArrayList<PostUserCommentDto> followedUsersPost = new ArrayList<>();
        for (String followedUser : user.getFollowedUsers()) {
            User followedUsr = getUserFromUsername(followedUser);
            for (String postId : followedUsr.getPosts()) {
                followedUsersPost.add(getPostUserComment(postId));
            }
        }
        followedUsersPost.sort(Comparator.comparing((PostUserCommentDto post) -> post.getPost().getTimestamp()));
        return followedUsersPost;
    }

    /**
     * Returns the given users and their followed users' posts, the post's comment and the poster's displayName and photoUrl in a chronological order
     */
    public ArrayList<PostUserCommentDto> getOwnAndFollowedUsersPostUserComments(String username) throws ExecutionException, InterruptedException {
        ArrayList<PostUserCommentDto> ownPosts = getOwnPostUserComments(username);
        ArrayList<PostUserCommentDto> followedUsersPosts = getFollowedUsersPostUserComments(username);
        ArrayList<PostUserCommentDto> ownAndFollowedUsersPosts = new ArrayList<>();
        ownAndFollowedUsersPosts.addAll(ownPosts);
        ownAndFollowedUsersPosts.addAll(followedUsersPosts);
        ownAndFollowedUsersPosts.sort(Comparator.comparing((PostUserCommentDto post) -> post.getPost().getTimestamp()));
        return ownAndFollowedUsersPosts;
    }

    private User getUserFromUsername(String username) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, username);
    }

}
