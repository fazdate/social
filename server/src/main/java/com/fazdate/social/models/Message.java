package com.fazdate.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.cloud.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageText;
    private Timestamp timestamp;
}
