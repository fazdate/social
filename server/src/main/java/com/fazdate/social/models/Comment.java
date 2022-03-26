package com.fazdate.social.models;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class Comment {
    private String commentId;
    private String commenterUsername;
    private String postId;
    private String text;
    private Date timestamp;
}
