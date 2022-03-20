package com.fazdate.social.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.cloud.Timestamp;

@Data
@NoArgsConstructor
public class Comment {
    private String commentId;
    private String commenterUsername;
    private String postId;
    private String text;
    private Timestamp timestamp;
}
