package ru.vsklamm.reddit.config;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(final String propertyName) {
        super("Property '%s' not found".formatted(propertyName));
    }
}
