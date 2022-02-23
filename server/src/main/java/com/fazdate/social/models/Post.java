package com.fazdate.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.cloud.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class Post {
    private String postId;
    private String posterUserId;
    private Timestamp timestamp;
    private String postText;
    private int numberOfLikes;
}
