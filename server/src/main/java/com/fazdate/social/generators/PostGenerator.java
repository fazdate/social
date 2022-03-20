package com.fazdate.social.generators;

import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.Post;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class PostGenerator {
    private final CommonDataGenerator commonDataGenerator;

    public Post generatePost(String username) throws ExecutionException, InterruptedException, IOException {
        Post post = new Post();
        post.setPostId(commonDataGenerator.generateId(Names.POSTS));
        post.setPosterUsername(username);
        post.setTimestamp(Timestamp.now().toDate());
        post.setText(commonDataGenerator.generateText(1, 25));
        post.setImgSrc(generatePostImage());
        post.setCommentIds(new ArrayList<>());
        return post;
    }

    private String generatePostImage() throws IOException {
        // If it's less than 10, then the post will have an image, otherwise it will be a text only post
        int doesThePostHaveAnImage = (int) (Math.random() * 5);
        if (doesThePostHaveAnImage > 0) {
            return commonDataGenerator.generatePhotoUrl("src/main/resources/postImages.txt");
        }
        return "";
    }



}
