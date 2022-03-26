package com.fazdate.social.helpers;

import com.fazdate.social.generators.CompleteDataGenerator;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class DatabaseRecreater {
    private final FirestoreService firestoreService;
    private final AuthService authService;
    private final CompleteDataGenerator completeDataGenerator;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseRecreater.class);

    /**
     * Deletes every user and their data and then it wil create 10 random user and their ddata.
     */
    public void recreateDatabase(int n) {
        deleteEveryUserAndTheirData();
        completeDataGenerator.generateRandomUsers(n);
        LOGGER.info("The db recreation finished");
    }

    private void deleteEveryUserAndTheirData() {
        authService.deleteEveryUser();
        firestoreService.deleteCollection(Names.USERS);
        firestoreService.deleteCollection(Names.POSTS);
        firestoreService.deleteCollection(Names.COMMENTS);
        firestoreService.deleteCollection(Names.MESSAGESLIST);
        firestoreService.deleteCollection(Names.MESSAGES);
    }

}
