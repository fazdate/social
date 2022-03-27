package com.fazdate.social.services.modelServices;

import com.fazdate.social.generators.CommonDataGenerator;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.MessagesList;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final FirestoreService firestoreService;
    private final CommonDataGenerator commonDataGenerator;

    /**
     * Adds a message to the correct users' messageList if they have talked before
     * Otherwise it will create a new messageList and add the message to it.
     */
    public void sendMessage(Message message) throws ExecutionException, InterruptedException {
        User sender = getUserFromDatabase(message.getSenderUsername());
        if (!sender.getMessagesList().contains(message.getMessagesListId())) {
            addFirstMessagesToUsers(sender, message);
        } else {
            addMessageToMessagesList(message);
        }
        LOGGER.info(message.getSenderUsername() + " sent a new message to " + message.getReceiverUsername());
    }

    /**
     * Returns a unique random id for a new message
     */
    public String generateMessageId() throws ExecutionException, InterruptedException {
        return commonDataGenerator.generateId(Names.MESSAGES);
    }

    /**
     * Returns a unique random id for a new messagesList
     */
    public String generateMessagesListId() throws ExecutionException, InterruptedException {
        return commonDataGenerator.generateId(Names.MESSAGESLIST);
    }

    private void addFirstMessagesToUsers(User user, Message message) throws ExecutionException, InterruptedException {
        MessagesList messagesList = MessagesList.builder()
                .messagesListId(commonDataGenerator.generateId(Names.MESSAGESLIST))
                .username1(message.getSenderUsername())
                .username2(message.getReceiverUsername())
                .messageIds(new ArrayList<>(Collections.singleton(message.getMessageId())))
                .build();

        firestoreService.addDocumentToCollection(Names.MESSAGESLIST, messagesList, messagesList.getMessagesListId());
        firestoreService.addDocumentToCollection(Names.MESSAGES, message, message.getMessageText());

        user.getMessagesList().add(messagesList.getMessagesListId());
        User user2 = getUserFromDatabase(message.getReceiverUsername());
        user2.getMessagesList().add(messagesList.getMessagesListId());
        updateUserInDatabase(user);
        updateUserInDatabase(user2);
    }

    private void addMessageToMessagesList(Message message) throws ExecutionException, InterruptedException {
        MessagesList messagesList = getMessagesList(message.getMessagesListId());
        messagesList.getMessageIds().add(message.getMessageId());
        updateMessagesInDatabase(messagesList);
        firestoreService.addDocumentToCollection(Names.MESSAGES, message, message.getMessageId());
    }

    public void createEmptyMessagesList(String username1, String username2) throws ExecutionException, InterruptedException {
        MessagesList messagesList = MessagesList.builder()
                .messagesListId(generateMessagesListId())
                .messageIds(new ArrayList<>())
                .username1(username1)
                .username2(username2)
                .build();

        User user1 = getUserFromDatabase(username1);
        user1.getMessagesList().add(messagesList.getMessagesListId());
        updateUserInDatabase(user1);

        User user2 = getUserFromDatabase(username2);
        user2.getMessagesList().add(messagesList.getMessagesListId());
        updateUserInDatabase(user2);

        firestoreService.addDocumentToCollection(Names.MESSAGESLIST, messagesList, messagesList.getMessagesListId());
    }

    /**
     * Returns a message from the given id
     */
    public Message getMessage(String messageId) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.MESSAGES, Message.class, messageId);
    }

    /**
     * Returns every message from a given messagesList
     */
    public Message[] getEveryMessageFromMessagesList(String messagesListId) throws ExecutionException, InterruptedException {
        ArrayList<Message> messages = new ArrayList<>();
        MessagesList messagesList = getMessagesList(messagesListId);
        for (String messageId : messagesList.getMessageIds()) {
            messages.add(getMessage(messageId));
        }
        return messages.toArray(new Message[messages.size()]);
    }

    /**
     * Gets the messagesList between the two given users.
     * If they don't already have one, it will create a new, empty one for them.
     */
    public String getMessagesListIdBetweenUsers(String username1, String username2) throws ExecutionException, InterruptedException {
        User user1 = getUserFromDatabase(username1);
        User user2 = getUserFromDatabase(username2);

        for (String messagesListId : user1.getMessagesList()) {
            if (user2.getMessagesList().contains(messagesListId)) {
                LOGGER.info(messagesListId);
                return messagesListId;
            }
        }

        // If no messagesList exists between the users,
        // it will create a new one, and then call this method again
        createEmptyMessagesList(username1, username2);
        getMessagesListIdBetweenUsers(username1, username2);

        return "";
    }

    /**
     * Checks if the given users have talked before
     */
    public boolean doesTheseUsersHaveMessages(String username1, String username2) throws ExecutionException, InterruptedException {
        User user1 = getUserFromDatabase(username1);
        ArrayList<String> messagesListIds = user1.getMessagesList();
        for (String id : messagesListIds) {
            MessagesList messagesList = getMessagesList(id);
            if ((messagesList.getUsername1().equals(username1) && messagesList.getUsername2().equals(username2))
                    || (messagesList.getUsername1().equals(username2) && messagesList.getUsername2().equals(username1))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a messageList from the given id
     */
    public MessagesList getMessagesList(String messagesListId) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.MESSAGESLIST, MessagesList.class, messagesListId);
    }

    private User getUserFromDatabase(String username) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, username);
    }

    private void updateUserInDatabase(User user) {
        firestoreService.updateDocumentInCollection(Names.USERS, user, user.getUsername());
    }

    private void updateMessagesInDatabase(MessagesList messagesList) {
        firestoreService.updateDocumentInCollection(Names.MESSAGESLIST, messagesList, messagesList.getMessagesListId());
    }

}
