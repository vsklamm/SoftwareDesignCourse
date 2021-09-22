package ru.vsklamm.reddit.client;

import org.junit.Assert;
import org.junit.Test;
import ru.vsklamm.reddit.model.RedditPost;

import java.util.List;

public class PostsListingParserTest {
    private final static String testResponse = """
            {
                "data": {
                    "after": "t3_pjk925",
                    "dist": 1,
                    "children": [{
                        "data": {
                            "selftext": "$GME",
                            "name": "name",
                            "created": 100.0
                        }
                    }]
                }
            }""";

    @Test
    public void parseResponse() {
        List<RedditPost> posts = PostsListingParser.getPosts(testResponse);

        Assert.assertEquals(1, posts.size());
        Assert.assertEquals(posts.get(0), new RedditPost("$GME", "name", 100));
    }
}

