package com.fazdate.social.services.firebaseServices;

import com.fazdate.social.models.User;
import com.google.firebase.auth.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public UserRecord getUserDataByUID(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
    }

    public ListUsersPage getEveryUser() throws FirebaseAuthException {
        return firebaseAuth.listUsers(null);
    }

    public void createUser(User user, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setUid(user.getUsername())
                .setEmail(user.getEmail())
                .setEmailVerified(true)
                .setPassword(password)
                .setDisplayName(user.getDisplayName());
        firebaseAuth.createUser(request);
    }

    public void deleteUser(String userId) throws FirebaseAuthException {
        firebaseAuth.deleteUser(userId);
    }

    public void deleteEveryUser() {
        try {
            ListUsersPage listUsersPage = getEveryUser();
            while (listUsersPage != null) {
                for (ExportedUserRecord user : listUsersPage.getValues()) {
                    deleteUser(user.getUid());
                    LOGGER.info("User with userId " + user.getUid() + " was deleted!");
                }
                listUsersPage.getNextPage();
            }
            // If there is no more users it can delete, then it will crash.
            // It will delete users, but we don't want it to crash, so we don't need to do anything with the exceptions
        } catch (RuntimeException | FirebaseAuthException e) {
            LOGGER.info("Every user has been deleted");
        }
    }

}
