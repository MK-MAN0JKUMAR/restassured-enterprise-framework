package framework.core.mock;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public final class WireMockManager {

    private static final ThreadLocal<WireMockServer> SERVER = new ThreadLocal<>();

    private WireMockManager() {}

    public static void start() {

        if (SERVER.get() != null && SERVER.get().isRunning()) {
            return;
        }

        WireMockServer server = new WireMockServer(options().dynamicPort());
        server.start();

        SERVER.set(server);

        System.out.println("WireMock started on port: " + server.port()
                + " | Thread: " + Thread.currentThread().getId());
    }

    public static void stop() {
        WireMockServer server = SERVER.get();

        if (server != null) {
            int port = server.port();

            if (server.isRunning()) {
                server.stop();
            }

            System.out.println("WireMock stopped on port: "
                    + port + " | Thread: " + Thread.currentThread().getId());
        }
        SERVER.remove();
    }

    public static WireMockServer getServer() {

        WireMockServer server = SERVER.get();

        if (server == null || !server.isRunning()) {
            throw new IllegalStateException("WireMock not started for this thread");
        }

        return server;
    }

    public static String baseUrl() {
        return "http://localhost:" + getServer().port();
    }
}