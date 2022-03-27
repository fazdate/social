package com.fazdate.social.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String postId;
    private String posterUsername;
    private Date timestamp;
    private String text;
    private String imgSrc;
    private List<String> usersThatLiked;
    private List<String> commentIds;
}
