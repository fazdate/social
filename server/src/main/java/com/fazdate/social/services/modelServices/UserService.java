package com.fazdate.social.services.modelServices;

import com.fazdate.social.generators.CommonDataGenerator;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Message;
import com.fazdate.social.models.Messages;
import com.fazdate.social.models.User;
import com.fazdate.social.services.firebaseServices.AuthService;
import com.fazdate.social.services.firebaseServices.FirestoreService;
import com.google.firebase.auth.FirebaseAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final FirestoreService firestoreService;
    private final AuthService authService;

    public UserService(FirestoreService firestoreService, AuthService authService) {
        this.firestoreService = firestoreService;
        this.authService = authService;
    }

    public void createUser(User user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        if (firestoreService.doesUserExists(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user was already created");
        }
        firestoreService.addDocumentToCollection(Names.USERS, user, user.getUserId());
        authService.createUser(user);
        LOGGER.info("User with userId: " + user.getUserId() + " was created!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        if (!firestoreService.doesUserExists(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't exists");
        }
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, userId);
    }

    public void updateUser(User user) throws ExecutionException, InterruptedException {
        if (!firestoreService.doesUserExists(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't exists");
        }
        updateUserInDatabase(user);
        LOGGER.info("User with userId: " + user.getUserId() + " was updated!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public void deleteUser(String userId) throws ExecutionException, InterruptedException, FirebaseAuthException {
        if (firestoreService.doesUserExists(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't exists");
        }
        firestoreService.deleteDocumentFromCollection(Names.USERS, userId);
        authService.deleteUser(userId);
        LOGGER.info("User with userId: " + userId + " was deleted!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public void addFriend(String userId, String friendId) throws ExecutionException, InterruptedException {
        if (!(firestoreService.doesUserExists(userId) && firestoreService.doesUserExists(friendId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = getUserFromDatabase(userId);
        user.getFriends().add(friendId);
        User friend = getUserFromDatabase(friendId);
        friend.getFriends().add(userId);
        updateUserInDatabase(user);
        updateUserInDatabase(friend);
        LOGGER.info("User with userId " + user.getUserId() + " has gotten a new friend!");
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public void addMessages(Message message) throws ExecutionException, InterruptedException {
        if (!(firestoreService.doesUserExists(message.getSenderId()) && firestoreService.doesUserExists(message.getReceiverId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't exists");
        }
        User sender = getUserFromDatabase(message.getSenderId());
        User receiver = getUserFromDatabase(message.getReceiverId());
        int index = returnTheIndexOfTheMessagesBetweenTheseUsers(sender, receiver);
        if (index == -1) {
            addNewMessagesToUsers(sender, receiver, message);
        } else {
            addNewMessageToMessages(sender, receiver, message, index);
        }
        updateUserInDatabase(sender);
        updateUserInDatabase(receiver);
        LOGGER.info("There has been a new message sent between " + sender.getUserId() + " and " + receiver.getUserId());
    }

    private int returnTheIndexOfTheMessagesBetweenTheseUsers(User user1, User user2) {
        List<Messages> messages = user1.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            Messages message = messages.get(i);
            if ((message.getUser1Id().equals(user1.getUserId()) && message.getUser2Id().equals(user2.getUserId()))
            || (message.getUser1Id().equals(user2.getUserId()) && message.getUser2Id().equals(user1.getUserId()))) {
                return i;
            }
        }
        return -1;
    }

    private void addNewMessagesToUsers(User user1, User user2, Message message) throws ExecutionException, InterruptedException {
        Messages messages = new Messages();
        messages.setMessagesId(CommonDataGenerator.generateId(Names.POSTS));
        messages.setUser1Id(user1.getUserId());
        messages.setUser2Id(user2.getUserId());
        messages.setMessages(List.of(message));

        user1.getMessages().add(messages);
        messages.setUser1Id(user2.getUserId());
        messages.setUser2Id(user1.getUserId());
        user2.getMessages().add(messages);
    }

    private void addNewMessageToMessages(User user1, User user2, Message message, int index) {
        user1.getMessages().get(index).getMessages().add(message);
        message.setReceiverId(user1.getUserId());
        message.setSenderId(user2.getUserId());
        user2.getMessages().get(index).getMessages().add(message);
    }


    private User getUserFromDatabase(String userId) throws ExecutionException, InterruptedException {
        return firestoreService.getObjectFromDocument(Names.USERS, User.class, userId);
    }

    private void updateUserInDatabase(User user) {
        firestoreService.updateDocumentInCollection(Names.USERS, user, user.getUserId());
    }
}
