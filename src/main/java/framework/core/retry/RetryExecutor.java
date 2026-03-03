package framework.core.retry;

import framework.core.http.HttpMethod;
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

    public static Response executeWithRetry(HttpMethod method,
                                            Supplier<Response> requestCall) {

        int attempt = 0;

        RetryContext.reset();

        while (true) {
            try {

                RetryContext.increment();

                Response response = requestCall.get();
                int status = response.statusCode();

                if (!shouldRetry(method, status) || attempt >= MAX_RETRIES) {
                    return response;
                }

                long delay = calculateDelay(attempt);
                log.warn("Retry attempt {} for {} due to status {}. Waiting {} ms",
                        attempt + 1, method, status, delay);

                sleep(delay);
                attempt++;

            } catch (Exception ex) {

                if (attempt >= MAX_RETRIES) {
                    throw ex;
                }

                long delay = calculateDelay(attempt);
                log.warn("Retry attempt {} for {} due to exception {}. Waiting {} ms",
                        attempt + 1, method, ex.getMessage(), delay);

                sleep(delay);
                attempt++;
            }
        }
    }

    private static boolean shouldRetry(HttpMethod method, int status) {

        boolean idempotent = method == HttpMethod.GET;

        if (!idempotent) return false;

        return status >= 500 || status == 408;
        // 429 removed intentionally (GitHub rate limit should NOT auto-retry)
    }

    private static long calculateDelay(int attempt) {
        return BASE_DELAY_MS * (1L << attempt);
    }

    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}