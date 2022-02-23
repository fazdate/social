package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.User;
import com.google.cloud.Timestamp;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.concurrent.ExecutionException;

import static com.fazdate.social.generators.CommonDataGenerator.generateId;

public class MessageGenerator {

    public static Message generateMessage(User sender, User receiver) throws ExecutionException, InterruptedException {
        Message message = new Message();
        message.setMessageId(generateId(Names.MESSAGE));
        message.setSenderId(sender.getUserId());
        message.setReceiverId(receiver.getUserId());
        message.setTimestamp(Timestamp.now());
        message.setMessageText(generateRandomMessageText());

        return message;
    }

    // Generates a random String that has at least 1, but at most 100 words.
    private static String generateRandomMessageText() {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getWords(1, 100);
    }
}
