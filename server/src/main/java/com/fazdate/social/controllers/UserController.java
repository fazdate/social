package com.fazdate.social.controllers;

import java.util.concurrent.ExecutionException;

import com.fazdate.social.models.Message;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.web.bind.annotation.*;

import com.fazdate.social.models.User;
import com.fazdate.social.services.modelServices.UserService;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody User user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        userService.createUser(user);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String userId) throws InterruptedException, ExecutionException {
        return userService.getUser(userId);
    }

    @PutMapping("/updateUser")
    public void updateUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        userService.updateUser(user);
    }

    @PutMapping("/deleteUser")
    public void deleteUser(@RequestParam String userId) throws ExecutionException, InterruptedException, FirebaseAuthException {
        userService.deleteUser(userId);
    }

    @PutMapping("/addFriend")
    public void addFriend(@RequestParam String userId, @RequestParam String friendId) throws ExecutionException, InterruptedException {
        userService.addFriend(userId, friendId);
    }

    @PutMapping("/addMessages")
    public void addMessages(@RequestBody Message message) throws ExecutionException, InterruptedException {
        userService.addMessages(message);
    }
}
