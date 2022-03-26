package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.MessagesList;
import com.fazdate.social.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class MessagesListGenerator {
    private final CommonDataGenerator commonDataGenerator;

    /**
     * Generates a messagesList between the given users and the given messages array
     */
    public MessagesList generateMessages(User user1, User user2, ArrayList<Message> messages) throws ExecutionException, InterruptedException {
        MessagesList messagesList = MessagesList.builder()
                .messagesListId(commonDataGenerator.generateId(Names.MESSAGESLIST))
                .username1(user1.getUsername())
                .username2(user2.getUsername())
                .messageIds(getMessageIds(messages))
                .build();

        return messagesList;
    }

    private ArrayList<String> getMessageIds(ArrayList<Message> messages) {
        ArrayList<String> messageIds = new ArrayList<>();
        for (Message message : messages) {
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }


}
