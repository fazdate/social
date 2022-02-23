package com.fazdate.social.services.firebaseServices;

import com.google.firebase.FirebaseApp;

public class ServiceLocator {
    private static final FirestoreService firestoreService = new FirestoreService();
    private static final FirebaseApp firebaseApp = MainService.getFirebaseApp();
    private static final AuthService authService = new AuthService();

    public static FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }

    public static AuthService getAuthService() {
        return authService;
    }

    public static FirestoreService getFirestoreService() {
        return firestoreService;
    }
}
