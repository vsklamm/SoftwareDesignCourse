package ru.vsklamm.reddit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vsklamm.reddit.model.RedditPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsListingParser {

    public static List<RedditPost> getPosts(final String jsonData) {
        final var mapper = new ObjectMapper();
        List<RedditPost> result = new ArrayList<>();
        try {
            final var root = mapper.readTree(jsonData);
            final var postsList = root.at("/data/children");
            final var postsAmount = root.at("/data/dist").asInt();
            for (int i = 0; i < postsAmount; i++) {
                final var post = postsList.at("/" + i + "/data");
                result.add(mapper.readValue(post.toString(), RedditPost.class));
            }
        } catch (IOException e) {
            throw new RuntimeException("Invalid listing json: " + jsonData);
        }
        return result;
    }
}
