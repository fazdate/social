package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.Messages;
import com.fazdate.social.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.fazdate.social.generators.CommonDataGenerator.generateId;

public class MessagesGenerator {

    public static Messages generateMessages(User user1, User user2) throws ExecutionException, InterruptedException {
        Messages messages = new Messages();
        messages.setMessagesId(generateId(Names.MESSAGES));
        messages.setUser1Id(user1.getUserId());
        messages.setUser2Id(user2.getUserId());
        messages.setMessages(generateMessageList(user1, user2));

        return messages;
    }

    // This will generate messages between user1 and user2.
    // At most, there will be maximum 10 messages from each user to the other.
    // For every message, there will be exactly one reply after it (excluding the last one)
    private static List<Message> generateMessageList(User user1, User user2) throws ExecutionException, InterruptedException {
        int numberOfMessages = (int) ((Math.random() * (10 - 1)) + 1);
        List<Message> messages = new ArrayList<>();

        for (int i = 0; i < numberOfMessages; i++) {
            messages.add(MessageGenerator.generateMessage(user1, user2));
            messages.add(MessageGenerator.generateMessage(user2, user1));
        }

        return messages;
    }
}
