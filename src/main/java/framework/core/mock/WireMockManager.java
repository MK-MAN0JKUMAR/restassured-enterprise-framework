package framework.core.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class WireMockManager {

    private static WireMockServer server;

    private WireMockManager() {}

    public static synchronized void start() {
        if (server != null && server.isRunning()) {
            return; // already started
        }

        server = new WireMockServer(options().dynamicPort());
        server.start();

        System.out.println("WireMock started on port: " + server.port());
    }

    public static synchronized void stop() {
        if (server != null && server.isRunning()) {
            server.stop();
            System.out.println("WireMock stopped");
        }
    }

    public static WireMockServer getServer() {
        if (server == null || !server.isRunning()) {
            throw new IllegalStateException("WireMock not started");
        }
        return server;
    }

    public static int port() {
        return getServer().port();
    }
}

