package framework.core.retry;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class RetryExecutor {

    private static final Logger log =
            LogManager.getLogger(RetryExecutor.class);

    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY_MS = 300;

    private RetryExecutor() {}

    public static Response executeWithRetry(Supplier<Response> requestCall) {

        Response response = null;

        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {

            response = requestCall.get();
            int status = response.statusCode();

            if (!shouldRetry(status) || attempt == MAX_RETRIES) {
                return response;
            }

            long delay = BASE_DELAY_MS * (1L << attempt);

            log.warn("Retry attempt {} for status {}. Waiting {} ms",
                    attempt + 1, status, delay);

            sleep(delay);
        }

        return response;
    }

    private static boolean shouldRetry(int status) {
        return status >= 500 || status == 429 || status == 408;
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
