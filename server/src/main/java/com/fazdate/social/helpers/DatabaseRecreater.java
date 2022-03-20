package com.fazdate.social.helpers;

import com.fazdate.social.generators.UserGenerator;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Component
public class DatabaseRecreater {
    private final FirestoreService firestoreService;
    private final AuthService authService;
    private final UserGenerator userGenerator;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseRecreater.class);

    public void recreateDatabase() throws ExecutionException, FirebaseAuthException, InterruptedException, IOException {
        deleteEveryUserAndTheirData();
        userGenerator.generateNRandomUser(10);
        LOGGER.info("The db recreation finished");
    }

    private void deleteEveryUserAndTheirData() {
        authService.deleteEveryUser();
        firestoreService.deleteCollection(Names.USERS);
        firestoreService.deleteCollection(Names.POSTS);
        firestoreService.deleteCollection(Names.COMMENTS);
    }

}
