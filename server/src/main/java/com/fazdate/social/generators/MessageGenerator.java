package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.User;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class MessageGenerator {
    private final CommonDataGenerator commonDataGenerator;

    /**
     * Generates a random message between the given users.
     * The messagesListId isn't set
     */
    public Message generateMessage(User sender, User receiver) throws ExecutionException, InterruptedException {
        Message message = Message.builder()
                .messageId(commonDataGenerator.generateId(Names.MESSAGES))
                .senderUsername(sender.getUsername())
                .receiverUsername(receiver.getUsername())
                .timestamp(Timestamp.now().toDate())
                .messageText(commonDataGenerator.generateText(1, 100))
                .build();

        return message;
    }

    /**
     * Generates random number of random messages between the given users.
     * There will be at most 10 messages from each user.
     * For every message, there will be exactly one reply after it (excluding the last one)
     */
    public ArrayList<Message> generateMessages(User user1, User user2) throws ExecutionException, InterruptedException {
        int numberOfMessages = (int) ((Math.random() * (10 - 1)) + 1);
        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0; i < numberOfMessages; i++) {
            messages.add(generateMessage(user1, user2));
            messages.add(generateMessage(user2, user1));
        }

        return messages;
    }
}
