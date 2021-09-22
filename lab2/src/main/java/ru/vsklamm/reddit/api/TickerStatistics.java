package ru.vsklamm.reddit.api;

import ru.vsklamm.reddit.client.RedditHttpClient;
import ru.vsklamm.reddit.model.RedditPost;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TickerStatistics {

    private final RedditHttpClient redditClient;

    public TickerStatistics(final RedditHttpClient redditClient) {
        this.redditClient = redditClient;
    }

    public List<Integer> getTickerStatistics(final String ticker, final int hoursNumber) {
        if (ticker.length() > 5) {
            throw new InvalidParameterException("Ticker must be a maximum of 5 characters long");
        }
        if (1 > hoursNumber || hoursNumber > 24) {
            throw new InvalidParameterException("Number of hours must be in [1, 24]");
        }
        final long endTimestamp = System.currentTimeMillis() / (long) 1000;
        List<Integer> result = new ArrayList<>();
        List<RedditPost> redditNewPosts = new ArrayList<>();
        long batchTickerCount;
        RedditPost lastPost = null;
        for (int i = 0; i < hoursNumber; i++) {
            final var beginHourTimestamp = endTimestamp - (long) (i + 1) * 60 * 60;
            final var endHourTimestamp = endTimestamp - (long) i * 60 * 60;

            Function<List<RedditPost>, Long> countTickers = list -> list.stream()
                    .filter(p -> beginHourTimestamp <= p.timestampCreated && p.timestampCreated < endHourTimestamp)
                    .filter(p -> p.title.matches("(?i).*\\b" + ticker + "\\b.*")).count();

            batchTickerCount = countTickers.apply(redditNewPosts);
            if (lastPost == null || beginHourTimestamp <= lastPost.timestampCreated) {
                redditNewPosts = redditClient.getNewPosts(lastPost == null ? "" : lastPost.name);
                if (!redditNewPosts.isEmpty()) {
                    lastPost = redditNewPosts.get(redditNewPosts.size() - 1);
                    batchTickerCount += countTickers.apply(redditNewPosts);
                }
            }
            result.add((int) batchTickerCount);
        }
        return result;
    }
}
