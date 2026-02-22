package framework.core.mock;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class WireMockManager {

    private static WireMockServer SERVER;

    private WireMockManager() {}

    public static synchronized void start() {

        if (SERVER != null && SERVER.isRunning()) {
            return;
        }

        SERVER = new WireMockServer(options().dynamicPort());
        SERVER.start();

        System.out.println("WireMock started on port: " + SERVER.port());
    }

    public static synchronized void stop() {

        if (SERVER != null && SERVER.isRunning()) {
            int port = SERVER.port();
            SERVER.stop();
            System.out.println("WireMock stopped on port: " + port);
        }

        SERVER = null;
    }

    public static boolean isRunning() {
        return SERVER != null && SERVER.isRunning();
    }

    public static WireMockServer getServer() {

        if (!isRunning()) {
            throw new IllegalStateException(
                    "WireMock server not started. Did you forget @BeforeClass?"
            );
        }

        return SERVER;
    }

    public static String baseUrl() {
        return "http://localhost:" + getServer().port();
    }
}