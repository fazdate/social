package com.fazdate.social.controllers;

import com.fazdate.social.dtos.MessagesListWithUserDataDto;
import com.fazdate.social.services.dtoServices.MessagesListWIthUserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class MessagesListWithUserDataController {
    private final MessagesListWIthUserDataService messagesListWIthUserDataService;

    @GetMapping("/getMessagesListWIthUserData")
    public MessagesListWithUserDataDto getMessagesListWIthUserData(@RequestParam String messagesListId) throws ExecutionException, InterruptedException {
        return messagesListWIthUserDataService.getMessagesListWIthUserData(messagesListId);
    }

    @GetMapping("/getUsersEveryMessagesListWithUserData")
    public MessagesListWithUserDataDto[] getUsersEveryMessagesListWithUserData(String username) throws ExecutionException, InterruptedException {
        return messagesListWIthUserDataService.getUsersEveryMessagesListWithUserData(username);
    }
}
