package ru.vsklamm.reddit.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.vsklamm.reddit.client.RedditHttpClient;
import ru.vsklamm.reddit.model.RedditPost;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class TickerStatisticsTest {
    private TickerStatistics statistics;

    @Mock
    private RedditHttpClient redditClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        statistics = new TickerStatistics(redditClient);
    }

    @Test
    public void testZeroResult() {
        when(redditClient.getNewPosts(anyString())).thenReturn(Collections.emptyList());
        final var tickerPerHourList = statistics.getTickerStatistics("GME", 1);
        Assert.assertEquals(1, tickerPerHourList.size());
        Assert.assertEquals(Integer.valueOf(0), tickerPerHourList.get(0));
    }

    @Test
    public void testOneResult() {
        final var response = List.of(
                new RedditPost("$GME", "", getCurrentHourTime(0))
        );
        when(redditClient.getNewPosts(anyString())).thenReturn(response);
        final var tickerPerHourList = statistics.getTickerStatistics("GME", 1);
        Assert.assertEquals(1, tickerPerHourList.size());
        Assert.assertEquals(Integer.valueOf(1), tickerPerHourList.get(0));
    }

    @Test
    public void testManyHours() {
        final var response = List.of(
                new RedditPost("$CLOV", "", getCurrentHourTime(0)),
                new RedditPost("$CLOV", "", getCurrentHourTime(1)),
                new RedditPost("$CLOV", "", getCurrentHourTime(3))
        );
        when(redditClient.getNewPosts(anyString())).thenReturn(response);
        final var tickerPerHourList = statistics.getTickerStatistics("CLOV", 3);
        Assert.assertEquals(3, tickerPerHourList.size());
        Assert.assertEquals(List.of(1, 1, 0), tickerPerHourList);
    }

    @Test
    public void testManyRequests() {
        final var response1 = List.of(
                new RedditPost("$GME", "", getCurrentHourTime(0)),
                new RedditPost("$GME", "", getCurrentHourTime(1)),
                new RedditPost("$GME", "", getCurrentHourTime(2))
        );
        final var response2 = List.of(
                new RedditPost("$GME", "", getCurrentHourTime(3)),
                new RedditPost("$GME", "", getCurrentHourTime(3)),
                new RedditPost("$GME", "", getCurrentHourTime(4))
        );
        when(redditClient.getNewPosts(anyString()))
                .thenReturn(response1)
                .thenReturn(response2);
        final var tickerPerHourList = statistics.getTickerStatistics("GME", 4);
        Assert.assertEquals(4, tickerPerHourList.size());
        Assert.assertEquals(List.of(1, 1, 1, 2), tickerPerHourList);
    }

    private long getCurrentHourTime(long shiftHours) {
        return System.currentTimeMillis() / (long) 1000 - shiftHours * 60 * 60 - 1;
    }
}
