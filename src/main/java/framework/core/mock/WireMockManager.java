package framework.core.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class WireMockManager {

    @Getter
    private static WireMockServer server;

    public static void start() {
        server = new WireMockServer(options().dynamicPort());
        server.start();
    }

    public static void stop() {
        if (server != null) server.stop();
    }

    public static int port() {
        return server.port();
    }
}
