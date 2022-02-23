package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.fazdate.social.services.firebaseServices.ServiceLocator.getFirestoreService;

public class CommonDataGenerator {

    // This will generate an 11 character numeric id
    public static String generateId(Names name) throws ExecutionException, InterruptedException {
        String chars = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        if (getFirestoreService().checkIfDocumentExistsInCollection(name, sb.toString())) {
            generateId(name);
        }

        return sb.toString();
    }
}
