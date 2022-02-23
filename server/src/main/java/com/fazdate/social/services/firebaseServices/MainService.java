package com.fazdate.social.services.firebaseServices;

import com.fazdate.social.SocialApplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class MainService {
    private static FirebaseApp firebaseApp;

    public static void initFirebase() throws IOException {
        ClassLoader classLoader = SocialApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public static FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
}
