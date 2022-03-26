package com.fazdate.social.controllers;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.fazdate.social.models.User;
import com.fazdate.social.services.modelServices.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public void createUser(@RequestBody User user, @RequestParam String password) throws FirebaseAuthException {
        userService.createUser(user, password);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String username) throws InterruptedException, ExecutionException {
        return userService.getUser(username);
    }

    @PostMapping("/updateUser")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @GetMapping("/getEveryUsername")
    public ArrayList<String> getEveryUsername() throws FirebaseAuthException {
        return userService.getEveryUsername();
    }

    @PutMapping("/followOrUnfollowUser")
    public void followOrUnFollowUser(@RequestParam String username, @RequestParam String anotherUsername) throws ExecutionException, InterruptedException {
        userService.followOrUnfollowUser(username, anotherUsername);
    }

    @GetMapping("/doesUserFollowAnotherUser")
    public boolean doesUserFollowAnotherUser(@RequestParam String username, @RequestParam String anotherUsername) throws ExecutionException, InterruptedException {
        return userService.doesUserFollowAnotherUser(username, anotherUsername);
    }

    @GetMapping("/getDisplayNameFromUsername")
    public String getDisplayNameFromUsername(String username) throws ExecutionException, InterruptedException {
        return userService.getDisplayNameFromUsername(username);
    }

}
