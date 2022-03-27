package com.fazdate.social.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String displayName;
    private String email;
    private String birthdate;
    private String photoURL;
    private ArrayList<String> posts;
    private ArrayList<String> followers;
    private ArrayList<String> followedUsers;
    private ArrayList<String> messagesList;
}
