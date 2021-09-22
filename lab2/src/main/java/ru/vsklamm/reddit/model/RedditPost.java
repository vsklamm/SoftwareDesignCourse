package ru.vsklamm.reddit.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditPost {
    public final String title;
    public final String name;
    public final long timestampCreated;

    @JsonCreator
    public RedditPost(
            @JsonProperty("selftext") final String title,
            @JsonProperty("name") final String name,
            @JsonProperty("created") final long timestampCreated) {
        this.title = title;
        this.name = name;
        this.timestampCreated = timestampCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedditPost redditPost = (RedditPost) o;

        return Objects.equals(name, redditPost.name)
                && Objects.equals(title, redditPost.title)
                && timestampCreated == redditPost.timestampCreated;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title != null ? title.hashCode() : 0;
        temp = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = timestampCreated;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

