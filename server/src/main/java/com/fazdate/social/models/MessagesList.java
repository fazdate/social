package com.fazdate.social.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagesList {
    private String messagesListId;
    private String username1;
    private String username2;
    private ArrayList<String> messageIds;
}
