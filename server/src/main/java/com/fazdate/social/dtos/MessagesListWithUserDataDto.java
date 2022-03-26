package com.fazdate.social.dtos;

import com.fazdate.social.models.MessagesList;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessagesListWithUserDataDto {
    private MessagesList messagesList;
    private String user1DisplayName;
    private String user1PhotoUrl;
    private String user2DisplayName;
    private String user2PhotoUrl;
}
