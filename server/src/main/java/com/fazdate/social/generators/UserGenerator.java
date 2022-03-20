package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Comment;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.fazdate.social.services.modelServices.PostService;
import com.fazdate.social.services.modelServices.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
public class UserGenerator {
    private final AuthService authService;
    private final FirestoreService firestoreService;
    private final PostService postService;
    private final UserService userService;
    private final CommonDataGenerator commonDataGenerator;
    private final PostGenerator postGenerator;
    private final CommentGenerator commentGenerator;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserGenerator.class);

    public void generateNRandomUser(int n) throws FirebaseAuthException, ExecutionException, InterruptedException, IOException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            users.add(generateUser());
        }
        updateUsers(users);
    }

    private User generateUser() throws FirebaseAuthException, ExecutionException, InterruptedException, IOException {
        String name = generateName();
        String username = generateUserName(name);
        String email = generateEmail(username);
        ArrayList<String> followers = generateFollowers(username);
        String birthdate = generateBirthdate();
        String photoUrl = generatePhotoUrl();

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setDisplayName(name);
        user.setFollowers(followers);
        user.setFollowedUsers(new ArrayList<>());
        user.setPhotoURL(photoUrl);

        userService.createUser(user, "123456");
        generatePosts(user);
        updateUser(user);

        return user;
    }

    private String generateName() {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getName();
    }

    private String generateUserName(String name) {
        // Making the name lowercase, and then removing tha accents from it.
        name = name.toLowerCase(Locale.ROOT);
        name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        String[] names = name.split(" ");
        String username = "@" + names[0] + "_" + names[1];

        // Checking if a username already exists.
        // If it exists, then it will append a bigger number after the username.
        int i = 0;
        String newUsername = null;
        while (true) {
            try {
                newUsername = (i == 0) ? username : username + i;
                authService.getUserDataByUID(newUsername); // This will throw an error, if the username doesn't exist.
            } catch (FirebaseAuthException e) {
                return newUsername;
            }
            i++;
        }
    }

    private String generateEmail(String username) {
        String withoutAtSymbol = username.substring(1);
        return withoutAtSymbol + "@social.com";
    }

    private String generateBirthdate() {
        String[] date = new String[3];

        date[0] = generateBirthYear();
        date[1] = generateBirthMonth();
        date[2] = generateBirthDay(date[1]);

        StringBuilder sb = new StringBuilder();
        for (String string : date) {
            sb.append(string).append(".");
        }

        return sb.toString();
    }

    private String generateBirthYear() {
        // This will generate users that are between the ages of 19 and 99
        return String.valueOf((int) (Math.random() * (1922 - 2003)) + 2003);
    }

    private String generateBirthMonth() {
        int month = (int) (Math.random() * (12 - 1)) + 1;
        return (month < 10) ? "0" + month : String.valueOf(month);
    }

    private String generateBirthDay(String month) {
        int day;
        int intMonth = Integer.parseInt(month);
        if (intMonth == 2) {
            day = (int) ((Math.random() * (28 - 1)) + 1);
        } else if (intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) {
            day = (int) ((Math.random() * (30 - 1)) + 1);
        } else {
            day = (int) ((Math.random() * (31 - 1)) + 1);
        }
        return (day < 10) ? "0" + day : String.valueOf(day);
    }

    private String generatePhotoUrl() throws IOException {
        return commonDataGenerator.generatePhotoUrl("src/main/resources/profileImages.txt");
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

    private void updateUsers(List<User> users) throws ExecutionException, InterruptedException {
        updateFollowers(users);
        updatePosts(users);
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

    private void updateUser(User user) throws ExecutionException, InterruptedException {
        userService.updateUser(user);
    }

    private void updatePost(Post post) throws ExecutionException, InterruptedException {
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
