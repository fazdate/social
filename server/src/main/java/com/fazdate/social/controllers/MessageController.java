package com.fazdate.social.controllers;

import com.fazdate.social.models.Message;
import com.fazdate.social.models.MessagesList;
import com.fazdate.social.services.modelServices.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @GetMapping("/getMessage")
    public Message getMessage(@RequestParam String messageId) throws ExecutionException, InterruptedException {
        return service.getMessage(messageId);
    }

    @GetMapping("/generateMessageId")
    public String generateMessageId() throws ExecutionException, InterruptedException {
        return service.generateMessageId();
    }

    @GetMapping("/getMessagesList")
    public MessagesList getMessagesList(@RequestParam String messagesListId) throws ExecutionException, InterruptedException {
        return service.getMessagesList(messagesListId);
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody Message message) throws ExecutionException, InterruptedException {
        service.sendMessage(message);
    }

    @GetMapping("/getEveryMessageFromMessagesList")
    public Message[] getEveryMessageFromMessagesList(@RequestParam String messagesListId) throws ExecutionException, InterruptedException {
        return service.getEveryMessageFromMessagesList(messagesListId);
    }

    @GetMapping("/getMessagesListIdBetweenUsers")
    public String getMessagesListIdBetweenUsers(String username1, String username2) throws ExecutionException, InterruptedException {
        return service.getMessagesListIdBetweenUsers(username1, username2);
    }

}
