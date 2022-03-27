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
public class Message {
    private String messageId;
    private String senderUsername;
    private String receiverUsername;
    private String messageText;
    private String messagesListId;
    private Date timestamp;
}
