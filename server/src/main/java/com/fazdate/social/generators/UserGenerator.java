package com.fazdate.social.generators;

import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.modelServices.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class UserGenerator {
    private final AuthService authService;
    private final UserService userService;

    private final CommonDataGenerator commonDataGenerator;

    /**
     * Generates N number of random users.
     */
    public List<User> generateNRandomUser(int n) {
        List<User> users = new ArrayList<>();
        try {
            for (int i = 0; i < n; i++) {
                users.add(generateUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private User generateUser() throws FirebaseAuthException, IOException {
        String name = generateName();
        String username = generateUserName(name);
        String email = generateEmail(username);

        User user = User.builder()
                .username(username)
                .email(email)
                .displayName(name)
                .birthdate(generateBirthdate())
                .followers(new ArrayList<>())
                .followedUsers(new ArrayList<>())
                .messagesList(new ArrayList<>())
                .photoURL(generatePhotoUrl())
                .build();
        userService.createUser(user, "123456");
        return user;
    }

    private String generateName() {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getName();
    }

    private String generateUserName(String name) {
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

    private String generateEmail(String username) {
        String withoutAtSymbol = username.substring(1);
        return withoutAtSymbol + "@social.com";
    }

    private String generateBirthdate() {
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

    private String generateBirthYear() {
        // This will generate users that are between the ages of 19 and 99
        return String.valueOf((int) (Math.random() * (1922 - 2003)) + 2003);
    }

    private String generateBirthMonth() {
        int month = (int) (Math.random() * (12 - 1)) + 1;
        return (month < 10) ? "0" + month : String.valueOf(month);
    }

    private String generateBirthDay(String month) {
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

    private String generatePhotoUrl() throws IOException {
        return commonDataGenerator.generatePhotoUrl("src/main/resources/profileImages.txt");
    }

}
