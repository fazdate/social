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

    /**
     * Generates a random Post from the given user
     */
    public Post generatePost(String username) throws ExecutionException, InterruptedException, IOException {
        Post post = Post.builder()
                .postId(commonDataGenerator.generateId(Names.POSTS))
                .posterUsername(username)
                .timestamp(Timestamp.now().toDate())
                .text(commonDataGenerator.generateText(1, 25))
                .imgSrc(generatePostImage())
                .commentIds(new ArrayList<>())
                .build();
        return post;
    }

    private String generatePostImage() throws IOException {
        // There is a 20% chance, that the post won't have an image
        int doesThePostHaveAnImage = (int) (Math.random() * 5);
        if (doesThePostHaveAnImage > 0) {
            return commonDataGenerator.generatePhotoUrl("src/main/resources/postImages.txt");
        }
        return "";
    }


}
