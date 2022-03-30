package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.*;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.fazdate.social.services.modelServices.MessageService;
import com.fazdate.social.services.modelServices.PostService;
import com.fazdate.social.services.modelServices.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
public class CompleteDataGenerator {
    private final PostGenerator postGenerator;
    private final CommentGenerator commentGenerator;
    private final MessageGenerator messageGenerator;
    private final MessagesListGenerator messagesListGenerator;
    private final UserGenerator userGenerator;

    private final FirestoreService firestoreService;
    private final PostService postService;
    private final UserService userService;
    private final MessageService messageService;

    private final static Logger LOGGER = LoggerFactory.getLogger(CompleteDataGenerator.class);

    private List<User> users = new ArrayList<>();

    public void generateRandomUsers(int n) {
        users = userGenerator.generateNRandomUser(n);
        generateInteractionsBetweenUsers();
    }

    private void generateInteractionsBetweenUsers() {
        try {
            for (User user : users) {
                generateFollowers(user.getUsername());
                generatePosts(user);
            }
            addInteractionsToPosts();
            generateMessages();
            for (User user : users) {
                updateUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateFollowers(String username) throws FirebaseAuthException {
        ArrayList<String> userIds = userService.getEveryUsername();
        int numberOfFollowers = generateNumberOfFollowers(userIds);
        User user = users.get(getIndexOfUserInList(username));

        while (numberOfFollowers != 0) {
            int randomNumber = (int) ((Math.random() * (userIds.size() - 1)) + 1);
            String followerId = userIds.get(randomNumber);

            // A user cannot follow themselves
            if (followerId.equals(username)) {
                followerId = userIds.get(0);
            }

            User follower = users.get(getIndexOfUserInList(followerId));
            follower.getFollowedUsers().add(user.getUsername());
            user.getFollowers().add(followerId);
            userIds.remove(followerId);
            numberOfFollowers--;
        }
    }

    private int generateNumberOfFollowers(List<String> userIds) {
        int min = 0;
        int max = userIds.size();
        return (int) (Math.random() * (max - min)) + min;
    }

    private void generatePosts(User user) throws IOException, ExecutionException, InterruptedException {
        int numberOfPosts = (int) ((Math.random() * (10 - 1)) + 1);
        ArrayList<String> posts = new ArrayList<>();
        for (int i = 0; i < numberOfPosts; i++) {
            Post post = postGenerator.generatePost(user.getUsername());
            firestoreService.addDocumentToCollection(Names.POSTS, post, post.getPostId());
            posts.add(post.getPostId());
        }
        user.setPosts(posts);
    }

    private void addInteractionsToPosts() throws ExecutionException, InterruptedException {
        updateNumberOfLikesOnPost();
        addCommentsToPosts();
    }

    private void updateNumberOfLikesOnPost() throws ExecutionException, InterruptedException {
        for (User user : users) {
            for (String postId : user.getPosts()) {
                Post post = postService.getPost(postId);
                generateUsersThatLike(user, post);
                updatePost(post);
            }
        }
    }

    private void generateUsersThatLike(User user, Post post) {
        int numberOfUsersThatLike = generateNumberOfLikesAndComments(user);
        ArrayList<String> followers = new ArrayList<>(user.getFollowers());
        ArrayList<String> usersThatLike = new ArrayList<>();

        while (numberOfUsersThatLike != 0) {
            int index = 0;
            if (numberOfUsersThatLike != 1 || followers.size() != 1) {
                index = (int) ((Math.random() * (followers.size() - 1)) + 1);
            }
            usersThatLike.add(followers.get(index));
            followers.remove(index);
            numberOfUsersThatLike--;
        }

        post.setUsersThatLiked(usersThatLike);
    }

    private void addCommentsToPosts() throws ExecutionException, InterruptedException {
        for (User user : users) {
            for (String postId : user.getPosts()) {
                Post post = postService.getPost(postId);
                int numberOfComments = generateNumberOfLikesAndComments(user);
                ArrayList<String> followers = user.getFollowers();

                while (numberOfComments != 0) {
                    int randomFollowerIdIndex = (int) (Math.random() * followers.size());
                    Comment comment = commentGenerator.generateComment(followers.get(randomFollowerIdIndex), postId);
                    firestoreService.addDocumentToCollection(Names.COMMENTS, comment, comment.getCommentId());
                    post.getCommentIds().add(comment.getCommentId());
                    followers.remove(randomFollowerIdIndex);
                    numberOfComments--;
                }
                updatePost(post);
            }
        }
    }

    private int generateNumberOfLikesAndComments(User user) {
        int numberOfFollowers = user.getFollowers().size();
        return numberOfFollowers > 1 ? (int) ((Math.random() * (numberOfFollowers - 1)) + 1) : 0;
    }

    private void generateMessages() throws ExecutionException, InterruptedException {
        for (User user : users) {
            for (String followerId : user.getFollowers()) {
                int index = getIndexOfUserInList(followerId);
                User follower = users.get(index);

                if (!doesTheseUsersHaveMessages(user, follower)) {
                    ArrayList<Message> messages = messageGenerator.generateMessages(user, follower);
                    MessagesList messagesList = messagesListGenerator.generateMessages(user, follower, messages);
                    uploadMessages(messages, messagesList.getMessagesListId());
                    uploadMessagesList(messagesList);
                    user.getMessagesList().add(messagesList.getMessagesListId());
                    follower.getMessagesList().add(messagesList.getMessagesListId());
                    LOGGER.info(user.getUsername() + " has messages with " + follower.getUsername());
                }
            }
        }
    }

    private boolean doesTheseUsersHaveMessages(User user1, User user2) throws ExecutionException, InterruptedException {
        for (String id : user1.getMessagesList()) {
            MessagesList messagesList = messageService.getMessagesList(id);
            if ((messagesList.getUsername1().equals(user1.getUsername()) && messagesList.getUsername2().equals(user2.getUsername()))
                    || (messagesList.getUsername1().equals(user2.getUsername()) && messagesList.getUsername2().equals(user1.getUsername()))) {
                return true;
            }
        }
        return false;
    }

    private void uploadMessages(ArrayList<Message> messages, String messagesListId) {
        for (Message message : messages) {
            message.setMessagesListId(messagesListId);
            firestoreService.addDocumentToCollection(Names.MESSAGES, message, message.getMessageId());
        }
    }

    private void uploadMessagesList(MessagesList messagesList) {
        firestoreService.addDocumentToCollection(Names.MESSAGESLISTS, messagesList, messagesList.getMessagesListId());
    }

    private void updateUser(User user) {
        userService.updateUser(user);
    }

    private void updatePost(Post post) {
        postService.updatePost(post);
    }

    private int getIndexOfUserInList(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }
}
