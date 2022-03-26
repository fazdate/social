package com.fazdate.social.services.dtoServices;

import com.fazdate.social.dtos.MessagesListWithUserDataDto;
import com.fazdate.social.models.MessagesList;
import com.fazdate.social.models.User;
import com.fazdate.social.services.modelServices.MessageService;
import com.fazdate.social.services.modelServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MessagesListWIthUserDataService {
    private final UserService userService;
    private final MessageService messageService;

    /**
     * Returns a messagesList with the two participants' displayNames and their photos
     */
    public MessagesListWithUserDataDto getMessagesListWIthUserData(String messagesListId) throws ExecutionException, InterruptedException {
        MessagesList messagesList = messageService.getMessagesList(messagesListId);
        User user1 = userService.getUser(messagesList.getUsername1());
        User user2 = userService.getUser(messagesList.getUsername2());
        return MessagesListWithUserDataDto.builder()
                .user1DisplayName(user1.getDisplayName())
                .user1PhotoUrl(user1.getPhotoURL())
                .user2DisplayName(user2.getDisplayName())
                .user2PhotoUrl(user2.getPhotoURL())
                .messagesList(messagesList)
                .build();
    }

    /**
     * Returns every messagesList that the given user participates in.
     * Also returns the participants' displayNames and their photos
     */
    public MessagesListWithUserDataDto[] getUsersEveryMessagesListWithUserData(String username) throws ExecutionException, InterruptedException {
        ArrayList<MessagesListWithUserDataDto> messagesListWithUserData = new ArrayList<>();
        User user = userService.getUser(username);
        for (String messagesListId : user.getMessagesList()) {
            messagesListWithUserData.add(getMessagesListWIthUserData(messagesListId));
        }
        return messagesListWithUserData.toArray(new MessagesListWithUserDataDto[messagesListWithUserData.size()]);
    }

}
