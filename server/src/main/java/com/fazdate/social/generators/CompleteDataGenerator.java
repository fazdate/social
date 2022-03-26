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

    public void generateRandomUsers(int n) {
        try {
            List<User> users = userGenerator.generateNRandomUser(n);
            for (User user : users) {
                user.setFollowers(generateFollowers(user.getUsername()));
                generatePosts(user);
            }
            generateInteractionsBetweenUsers(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> generateFollowers(String username) throws FirebaseAuthException {
        return generateRandomFollowers(username);
    }

    private ArrayList<String> generateRandomFollowers(String username) throws FirebaseAuthException {
        ArrayList<String> userIds = userService.getEveryUsername();
        int numberOfFollowers = generateNumberOfFollowers(userIds);
        ArrayList<String> followers = new ArrayList<>();
        while (numberOfFollowers != 0) {
            int randomNumber = (int) ((Math.random() * (userIds.size() - 1)) + 1);
            String potentialFollower = userIds.get(randomNumber);
            // A user cannot follow themselves
            if (potentialFollower.equals(username)) {
                potentialFollower = userIds.get(0);
            }
            followers.add(potentialFollower);
            userIds.remove(potentialFollower);
            numberOfFollowers--;
        }
        return followers;
    }

    private int generateNumberOfFollowers(List<String> userIds) {
        int min = 0;
        int max = userIds.size();
        return (int) (Math.random() * (max - min)) + min;
    }

    private void generatePosts(User user) throws ExecutionException, InterruptedException, IOException {
        int numberOfPosts = (int) ((Math.random() * (10 - 1)) + 1);
        ArrayList<String> posts = new ArrayList<>();
        for (int i = 0; i < numberOfPosts; i++) {
            Post post = postGenerator.generatePost(user.getUsername());
            firestoreService.addDocumentToCollection(Names.POSTS, post, post.getPostId());
            posts.add(post.getPostId());
        }
        user.setPosts(posts);
    }

    private void generateInteractionsBetweenUsers(List<User> users) throws ExecutionException, InterruptedException {
        updateFollowers(users);
        updatePosts(users);
        generateMessages(users);
        for (User user : users) {
            updateUser(user);
        }
    }

    private void updateFollowers(List<User> users) {
        for (int i = users.size(); i != 0; i--) {
            User user1 = users.get(i - 1);
            for (String follower : user1.getFollowers()) {
                User user2 = users.get(getIndexOfUserInList(users, follower));
                List<String> user2FollowedUsers = user2.getFollowedUsers();
                if (!user2FollowedUsers.contains(user1.getUsername())) {
                    user2.getFollowedUsers().add(user1.getUsername());
                }
                LOGGER.info("User with userId " + follower + " had their followers updated");
            }
        }
    }

    private void updatePosts(List<User> users) throws ExecutionException, InterruptedException {
        updateNumberOfLikesOnPost(users);
        addCommentsToPosts(users);
    }

    private void updateNumberOfLikesOnPost(List<User> users) throws ExecutionException, InterruptedException {
        for (User user : users) {
            for (String postId : user.getPosts()) {
                Post post = postService.getPost(postId);
                post.setUsersThatLiked(generateUsersThatLike(user));
                updatePost(post);
            }
        }
    }

    private void addCommentsToPosts(List<User> users) throws ExecutionException, InterruptedException {
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


    private ArrayList<String> generateUsersThatLike(User user) {
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

        return usersThatLike;
    }

    private int generateNumberOfLikesAndComments(User user) {
        int numberOfFollowers = user.getFollowers().size();
        return numberOfFollowers > 1 ? (int) ((Math.random() * (numberOfFollowers - 1)) + 1) : 0;
    }

    private void generateMessages(List<User> users) throws ExecutionException, InterruptedException {
        for (User user : users) {
            ArrayList<String> followers = user.getFollowers();
            for (String followerId : followers) {
                int index = getIndexOfUserInList(users, followerId);
                User follower = users.get(index);
                if (!messageService.doesTheseUsersHaveMessages(user.getUsername(), follower.getUsername())) {
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

    private void uploadMessages(ArrayList<Message> messages, String messagesListId) {
        for (Message message : messages) {
            message.setMessagesListId(messagesListId);
            firestoreService.addDocumentToCollection(Names.MESSAGES, message, message.getMessageId());
        }
    }

    private void uploadMessagesList(MessagesList messagesList) {
        firestoreService.addDocumentToCollection(Names.MESSAGESLIST, messagesList, messagesList.getMessagesListId());
    }

    private void updateUser(User user) {
        userService.updateUser(user);
    }

    private void updatePost(Post post) {
        postService.updatePost(post);
    }

    private int getIndexOfUserInList(List<User> users, String userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(userId)) {
                return i;
            }
        }
        return -1;
    }
}
