package com.fazdate.social.models;

import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class User {
    private String username;
    private String displayName;
    private String email;
    private String birthdate;
    private String photoURL;
    private ArrayList<String> posts;
    private ArrayList<String> followers;
    private ArrayList<String> followedUsers;
}
