package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class CommonDataGenerator {
    private final FirestoreService firestoreService;

    /**
     * Generates a random 11 long numeric ID in string.
     * It will check the given collection to make sure the ID isn't already used.
     */
    public String generateId(Names name) throws ExecutionException, InterruptedException {
        String chars = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        if (firestoreService.checkIfDocumentExistsInCollection(name, sb.toString())) {
            generateId(name);
        }

        return sb.toString();
    }

    /**
     * Returns with a random URL from the given text file
     *
     * @param fileName - Has to be given from the src folder
     */
    public String generatePhotoUrl(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        String[] images = lines.toArray(new String[lines.size()]);

        Random random = new Random();
        return images[random.nextInt(images.length)];
    }

    /**
     * Return with a String that contains a random number of words
     *
     * @param min - The minimum of the words
     * @param max - The maximum of the words
     */
    public String generateText(int min, int max) {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getWords(min, max);
    }

}
