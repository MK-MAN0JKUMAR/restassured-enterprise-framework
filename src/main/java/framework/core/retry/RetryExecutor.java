package framework.core.retry;

import io.restassured.response.Response;

import java.util.function.Supplier;

public final class RetryExecutor {

    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 500;

    private RetryExecutor() {}

    public static Response executeWithRetry(Supplier<Response> requestCall) {

        Response response = null;

        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {

            response = requestCall.get();

            // Retry only for server errors (5xx)
            if (response.statusCode() < 500) {
                return response;
            }

            sleep();
        }

        return response;
    }

    private static void sleep() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
