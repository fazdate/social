package com.fazdate.social.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Message {
    private String messageId;
    private String senderUsername;
    private String receiverUsername;
    private String messageText;
    private String messagesListId;
    private Date timestamp;
}
