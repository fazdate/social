package com.fazdate.social.helpers;

import com.fazdate.social.generators.UserGenerator;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.fazdate.social.services.firebaseServices.ServiceLocator;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("SpellCheckingInspection")
public class DatabaseRecreater {
    private static final FirestoreService firestoreService = ServiceLocator.getFirestoreService();
    private static final AuthService authService = ServiceLocator.getAuthService();

    public static void recreateDatabase() throws ExecutionException, FirebaseAuthException, InterruptedException {
        deleteEveryUserAndTheirData();
        UserGenerator.generateNRandomUser(10);
    }

    private static void deleteEveryUserAndTheirData() {
        authService.deleteEveryUserAndTheirData(Names.USERS);
        firestoreService.deleteCollection(Names.POSTS);
        firestoreService.deleteCollection(Names.MESSAGES);
    }

}
