package ru.vsklamm.reddit.client;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ru.vsklamm.reddit.config.RedditConfig;
import ru.vsklamm.reddit.model.RedditPost;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RedditHttpClient implements RedditClient {
    private final String ENCODING = "UTF-8";

    public final String WALLSTREETBETS_NEW_JSON =
            RedditClient.OAUTH_API_DOMAIN +
                    "/r/wallstreetbets/new.json" +
                    "?limit=%d" +
                    "&after=%s";

    private final HttpClient client;
    private final RedditConfig config;
    private final RedditToken token;

    public RedditHttpClient(final RedditConfig config) {
        this.client = HttpClients.createDefault();
        this.config = config;
        this.token = getAuthToken();
    }

    public RedditHttpClient(final RedditConfig config, final RedditToken token) {
        this.client = HttpClients.createDefault();
        this.config = config;
        this.token = token;
    }

    @Override
    public List<RedditPost> getNewPosts(final String afterName) {
        final var getRequest = new HttpGet(WALLSTREETBETS_NEW_JSON.formatted(100, afterName));
        final var response = request(token, getRequest);
        return PostsListingParser.getPosts(response);
    }

    public String request(final RedditToken token, final HttpRequestBase httpRequest) {
        try {
            httpRequest.setHeader(HttpHeaders.AUTHORIZATION, token.getTokenType() + " " + token.getAccessToken());
            httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpRequest.addHeader(HttpHeaders.USER_AGENT, config.getAppName() + " by " + config.getAuthor());
            final var response = client.execute(httpRequest);
            return EntityUtils.toString(response.getEntity(), ENCODING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public RedditToken getAuthToken() {
        if (token != null) {
            return token;
        }
        try {
            final var clientIdSecret = Base64.getEncoder().encodeToString((config.getAppId() + ":" + config.getSecret()).getBytes());
            final var token = new RedditToken(clientIdSecret, "Basic");

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
            postParameters.add(new BasicNameValuePair("duration", "temporary"));

            final var postRequest = new HttpPost(ACCESS_TOKEN_REQUEST);
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, ENCODING));
            final var jsonResponse = request(token, postRequest);
            return RedditToken.fromJson(jsonResponse);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
