package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.Messages;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.fazdate.social.services.firebaseServices.ServiceLocator;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class UserGenerator {
    private static final AuthService authService = ServiceLocator.getAuthService();
    private static final FirestoreService firestoreService = ServiceLocator.getFirestoreService();
    final static Logger LOGGER = LoggerFactory.getLogger(UserGenerator.class);

    public static void generateNRandomUser(int n) throws FirebaseAuthException, ExecutionException, InterruptedException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            users.add(generateUser());
        }
        updateUsers(users);
    }

    private static User generateUser() throws FirebaseAuthException, ExecutionException, InterruptedException {
        String name = generateName();
        String username = generateUserName(name);
        String email = generateEmail(username);
        List<String> friends = generateFriends(username);
        String birthdate = generateBirthdate();

        User user = new User();
        user.setUserId(username);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setDisplayName(name);
        user.setFriends(friends);

        createUser(user);
        generateMessages(user, friends);
        generatePosts(user);
        updateUser(user);

        return user;
    }

    private static String generateName() {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getName();
    }

    private static String generateUserName(String name) {
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

    private static String generateEmail(String username) {
        String withoutAtSymbol = username.substring(1);
        return withoutAtSymbol + "@social.com";
    }

    private static String generateBirthdate() {
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

    private static String generateBirthYear() {
        // This will generate users that are between the ages of 19 and 99
        return String.valueOf((int) (Math.random() * (1922 - 2003)) + 2003);
    }

    private static String generateBirthMonth() {
        int month = (int) (Math.random() * (12 - 1)) + 1;
        return (month < 10) ? "0" + month : String.valueOf(month);
    }

    private static String generateBirthDay(String month) {
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

    private static List<String> generateFriends(String username) throws FirebaseAuthException {
        List<String> userIds = getEveryUserId();
        return generateRandomFriends(userIds, username);
    }

    private static List<String> getEveryUserId() throws FirebaseAuthException {
        ListUsersPage listUsersPage = authService.getEveryUser();
        List<String> userIds = new ArrayList<>();
        for (ExportedUserRecord user : listUsersPage.getValues()) {
            userIds.add(user.getUid());
        }
        return userIds;
    }

    private static List<String> generateRandomFriends(List<String> userIds, String username) {
        int numberOfFriends = generateNumberOfFriends(userIds);
        List<String> friends = new ArrayList<>();
        while (numberOfFriends != 0) {
            int randomNumber = (int) ((Math.random() * (userIds.size() - 1)) + 1);
            String potentialFriend = userIds.get(randomNumber);
            // A user cannot be their own friend
            if (potentialFriend.equals(username)) {
                potentialFriend = userIds.get(0);
            }
            friends.add(potentialFriend);
            userIds.remove(potentialFriend);
            numberOfFriends--;
        }
        return friends;
    }

    private static int generateNumberOfFriends(List<String> userIds) {
        int min = 0;
        int max = userIds.size();
        return (int) (Math.random() * (max - min)) + min;
    }

    private static void createUser(User user) throws FirebaseAuthException {
        authService.createUser(user);
        LOGGER.info("User with userId: " + user.getUserId() + " has been created!");
        firestoreService.addDocumentToCollection(Names.USERS, user, user.getUserId());
        LOGGER.info("User with userId: " + user.getUserId() + " has been added to the database!");
    }

    private static void generateMessages(User user, List<String> friends) throws ExecutionException, InterruptedException {
        List<Messages> messages = new ArrayList<>();
        for (String userId : friends) {
            User friend = firestoreService.getObjectFromDocument(Names.USERS, User.class, userId);
            if (!firestoreService.checkIfMessagesHasTheseUsers(user, friend)) {
                messages.add(MessagesGenerator.generateMessages(user, friend));
            }
        }
        user.setMessages(messages);
    }

    private static void generatePosts(User user) throws ExecutionException, InterruptedException {
        int numberOfPosts = (int) ((Math.random() * (10 - 1)) + 1);
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < numberOfPosts; i++) {
            posts.add(PostGenerator.generatePost(user));
        }
        user.setPosts(posts);
    }

    private static void updateUsers(List<User> users) {
        updateMessages(users);
        updateFriends(users);
        updateNumberOfLikesOnPost(users);
        for (User user : users) {
            updateUser(user);
        }
    }

    // TODO: Sometimes this updates the messages twice, look at @Burt_calderon
    private static void updateMessages(List<User> users) {
        for (int i = users.size(); i != 0; i--) {
            User user1 = users.get(i - 1);
            List<Messages> user1Messages = user1.getMessages();
            for (Messages messages : user1Messages) {
                User user2 = users.get(getIndexOfUserInList(users, messages.getUser2Id()));
                messages.setUser2Id(user1.getUserId());
                messages.setUser1Id(user2.getUserId());
                user2.getMessages().add(messages);
                LOGGER.info("User with userId " + user2.getUserId() + " had their messages updated");
            }
        }
    }


    private static void updateFriends(List<User> users) {
        for (int i = users.size(); i != 0; i--) {
            User user1 = users.get(i - 1);
            List<String> user1Friends = user1.getFriends();
            for (String friend : user1Friends) {
                User user2 = users.get(getIndexOfUserInList(users, friend));
                List<String> friends = user2.getFriends();
                if (!friends.contains(user1.getUserId())) {
                    user2.getFriends().add(user1.getUserId());
                }
                LOGGER.info("User with userId " + friend + " had their friends updated");
            }
        }
    }

    private static void updateNumberOfLikesOnPost(List<User> users) {
        for (User user : users) {
            for (Post post : user.getPosts()) {
                post.setNumberOfLikes(generateNumberOfLikes(user));
                LOGGER.info("Post with the id " + post.getPostId() + " had the number of likes updated!");
            }
        }
    }

    // Maximum number is the number of the user's friend. The minimum number is 1.
    // If the user only has 1 or 0 friends, then the number of friends will be returned.
    private static int generateNumberOfLikes(User user){
        int numberOfFriends = user.getFriends().size();
        return numberOfFriends > 1 ? (int) ((Math.random() * (numberOfFriends - 1)) + 1) : numberOfFriends;
    }

    private static void updateUser(User user) {
        firestoreService.updateDocumentInCollection(Names.USERS, user, user.getUserId());
    }

    private static int getIndexOfUserInList(List<User> users, String userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(userId)) {
                return i;
            }
        }
        return -1;
    }
}
