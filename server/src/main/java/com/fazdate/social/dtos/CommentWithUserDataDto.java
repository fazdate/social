package com.fazdate.social.dtos;

import com.fazdate.social.models.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentWithUserDataDto {
    private Comment comment;
    private String displayName;
    private String photoUrl;
}
