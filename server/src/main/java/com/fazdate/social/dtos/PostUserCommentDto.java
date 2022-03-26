package com.fazdate.social.dtos;

import com.fazdate.social.models.Comment;
import com.fazdate.social.models.Post;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostUserCommentDto {
    private Post post;
    private String photoUrl;
    private String displayName;
    private Comment[] comments;
}
