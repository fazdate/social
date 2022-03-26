package com.fazdate.social.models;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class MessagesList {
    private String messagesListId;
    private String username1;
    private String username2;
    private ArrayList<String> messageIds;
}
