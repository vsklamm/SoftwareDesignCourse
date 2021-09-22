package ru.vsklamm.reddit.client;

import org.apache.http.client.methods.HttpRequestBase;
import ru.vsklamm.reddit.model.RedditPost;

import java.util.List;

public interface RedditClient {

    String OAUTH_API_DOMAIN = "https://oauth.reddit.com";

    String ACCESS_TOKEN_REQUEST = "https://www.reddit.com/api/v1/access_token";

    List<RedditPost> getNewPosts(final String afterName);

    String request(final RedditToken token, final HttpRequestBase httpRequest);

    RedditToken getAuthToken();
}
