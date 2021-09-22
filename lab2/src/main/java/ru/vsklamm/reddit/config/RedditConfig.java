package ru.vsklamm.reddit.config;

import java.util.Properties;

public class RedditConfig {
    private static final String APP_ID_PROPERTY_NAME = "app_id";
    private static final String SECRET_PROPERTY_NAME = "secret";
    private static final String APP_NAME_PROPERTY_NAME = "app_name";
    private static final String AUTHOR_PROPERTY_NAME = "author";

    private final String appId;
    private final String secret;
    private final String appName;
    private final String author;

    public RedditConfig(final Properties properties) {
        this.appId = getOrThrow(properties, APP_ID_PROPERTY_NAME);
        this.secret = getOrThrow(properties, SECRET_PROPERTY_NAME);
        this.appName = getOrThrow(properties, APP_NAME_PROPERTY_NAME);
        this.author = getOrThrow(properties, AUTHOR_PROPERTY_NAME);
    }

    private String getOrThrow(final Properties properties, final String propertyName) {
        final var value = properties.getProperty(propertyName);
        if (value == null) {
            throw new PropertyNotFoundException(propertyName);
        }
        return value;
    }

    public String getAppId() {
        return appId;
    }

    public String getSecret() {
        return secret;
    }

    public String getAppName() {
        return appName;
    }

    public String getAuthor() {
        return author;
    }
}
