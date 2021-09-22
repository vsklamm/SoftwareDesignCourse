package ru.vsklamm.reddit;

import ru.vsklamm.reddit.api.TickerStatistics;
import ru.vsklamm.reddit.client.RedditHttpClient;
import ru.vsklamm.reddit.config.PropertiesLoader;
import ru.vsklamm.reddit.config.RedditConfig;

import java.security.InvalidParameterException;

public class Main {
    public static void main(String[] args) {
        var redditConfig = new RedditConfig(PropertiesLoader.loadProperties("src/main/resources/app.properties"));
        var redditClient = new RedditHttpClient(redditConfig);
        var tickerStatistics = new TickerStatistics(redditClient);
        try {
            var tickerStatisticsList = tickerStatistics.getTickerStatistics("CLOV", 24);
            tickerStatisticsList.forEach(e -> System.out.print(e + " "));
        } catch (InvalidParameterException e) {
            System.err.println(e.getMessage());
        }
    }
}
