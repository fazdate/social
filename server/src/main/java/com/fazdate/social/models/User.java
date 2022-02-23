package com.fazdate.social.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class User {
    private String userId;
    private String displayName;
    private String email;
    private String birthdate;
    private List<Messages> messages;
    private List<Post> posts;
    private List<String> friends;
}
