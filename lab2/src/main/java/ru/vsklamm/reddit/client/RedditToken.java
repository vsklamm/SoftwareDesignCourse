package ru.vsklamm.reddit.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditToken {

    private final String accessToken;

    private final String tokenType;

    @JsonCreator
    public RedditToken(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public static RedditToken fromJson(final String jsonData) {
        try {
            final var om = new ObjectMapper();
            return om.readValue(jsonData, RedditToken.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid token json: " + jsonData);
        }
    }
}