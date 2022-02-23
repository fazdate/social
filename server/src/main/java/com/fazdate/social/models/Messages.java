package com.fazdate.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Messages {
    private String messagesId;
    private String user1Id;
    private String user2Id;
    private List<Message> messages;
}
