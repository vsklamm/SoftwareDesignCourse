package ru.vsklamm.reddit.api;

import org.apache.http.client.methods.HttpGet;
import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.Test;
import com.xebialabs.restito.server.StubServer;
import ru.vsklamm.reddit.client.RedditHttpClient;
import ru.vsklamm.reddit.client.RedditToken;
import ru.vsklamm.reddit.config.PropertiesLoader;
import ru.vsklamm.reddit.config.RedditConfig;

import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;

public class TickerStatisticsStubServerTest {

    private static final int PORT = 3535;

    final RedditConfig redditConfig = new RedditConfig(PropertiesLoader.loadProperties("src/main/resources/app.properties"));
    final RedditToken redditToken = new RedditToken("token", "type");

    RedditHttpClient redditHttpClient;

    @Test
    public void getTest() {
        withStubServer(s -> {
                    whenHttp(s)
                            .match(method(Method.GET))
                            .then(stringContent("result"));

                    redditHttpClient = new RedditHttpClient(redditConfig, redditToken);
                    final var getRequest = new HttpGet("http://localhost:%d/test".formatted(PORT));
                    final var response = redditHttpClient.request(redditToken, getRequest);
                    Assert.assertEquals(response, "result");
                }
        );
    }

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(TickerStatisticsStubServerTest.PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
