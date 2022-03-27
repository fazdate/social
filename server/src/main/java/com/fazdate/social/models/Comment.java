package com.fazdate.social.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String commentId;
    private String commenterUsername;
    private String postId;
    private String text;
    private Date timestamp;
}
