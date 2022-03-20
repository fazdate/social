package com.fazdate.social.services.modelServices;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final FirestoreService firestoreService;
    private final AuthService authService;

    public void createUser(User user, String password) throws FirebaseAuthException {
        firestoreService.addDocumentToCollection(Names.USERS, user, user.getUsername());
        authService.createUser(user, password);
        LOGGER.info("User with username: " + user.getUsername() + " was created!");
    }

    public User getUser(String username) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, username);
    }

    public void updateUser(User user) {
        updateUserInDatabase(user);
        LOGGER.info(user.getUsername() + " was updated!");
    }

    public void deleteUser(String username) throws FirebaseAuthException {
        firestoreService.deleteDocumentFromCollection(Names.USERS, username);
        authService.deleteUser(username);
        LOGGER.info(username + " was deleted!");
    }

    public void followOrUnfollowUser(String username, String anotherUsername) throws ExecutionException, InterruptedException {
        User user = getUserFromDatabase(username);
        User anotherUser = getUserFromDatabase(anotherUsername);
        if (doesUserFollowAnotherUser(username, anotherUsername)) {
            unfollowUser(user, anotherUser);
        } else {
            followUser(user, anotherUser);
        }
    }

    public boolean doesUserFollowAnotherUser(String username, String anotherUsername) throws ExecutionException, InterruptedException {
        User user = getUserFromDatabase(username);
        for (String name : user.getFollowers()) {
            if (name.equals(anotherUsername)) {
                return true;
            }
        }
        return false;
    }

    private void followUser(User user, User anotherUser) {
        user.getFollowers().add(anotherUser.getUsername());
        anotherUser.getFollowedUsers().add(user.getUsername());
        updateUserInDatabase(user);
        updateUserInDatabase(anotherUser);
        LOGGER.info(anotherUser.getUsername() + " has followed " + user.getUsername());
    }

    private void unfollowUser(User user, User anotherUser) {
        user.getFollowers().remove(anotherUser.getUsername());
        anotherUser.getFollowedUsers().remove(user.getUsername());
        updateUserInDatabase(user);
        updateUserInDatabase(anotherUser);
        LOGGER.info(anotherUser.getUsername() + " has unfollowed + " + user.getUsername());
    }

    public ArrayList<String> getEveryUsername() throws FirebaseAuthException {
        ListUsersPage listUsersPage = authService.getEveryUser();
        ArrayList<String> usernames = new ArrayList<>();
        for (ExportedUserRecord user : listUsersPage.getValues()) {
            usernames.add(user.getUid());
        }
        return usernames;
    }


    private User getUserFromDatabase(String username) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, username);
    }

    private void updateUserInDatabase(User user) {
        firestoreService.updateDocumentInCollection(Names.USERS, user, user.getUsername());
    }
}
