package framework.core.mock;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class WireMockManager {

    private static WireMockServer SERVER;

    private static final AtomicInteger ACTIVE_USERS = new AtomicInteger(0);

    private WireMockManager() {}

    public static synchronized void start() {

        ACTIVE_USERS.incrementAndGet();

        if (SERVER != null && SERVER.isRunning()) {
            return;
        }

        SERVER = new WireMockServer(options().dynamicPort());
        SERVER.start();

        System.out.println("WireMock started on port: " + SERVER.port());
    }

    public static synchronized void stop() {

        int remaining = ACTIVE_USERS.decrementAndGet();

        if (remaining > 0) {
            return;
        }

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