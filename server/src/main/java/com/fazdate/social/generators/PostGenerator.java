package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Post;
import com.fazdate.social.models.User;
import com.google.cloud.Timestamp;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.util.concurrent.ExecutionException;

import static com.fazdate.social.generators.CommonDataGenerator.generateId;

public class PostGenerator {

    public static Post generatePost(User user) throws ExecutionException, InterruptedException {
        Post post = new Post();
        post.setPostId(generateId(Names.POSTS));
        post.setPosterUserId(user.getUserId());
        post.setTimestamp(Timestamp.now());
        post.setPostText(generatePostText());
        return post;
    }

    // Generates a random string consisting of 1-25 words.
    private static String generatePostText() {
        Lorem lorem = LoremIpsum.getInstance();
        return lorem.getWords(1, 25);
    }

}
