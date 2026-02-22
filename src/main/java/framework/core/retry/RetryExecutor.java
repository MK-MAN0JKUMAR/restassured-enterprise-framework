package framework.core.retry;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class RetryExecutor {

    private static final Logger log =
            LogManager.getLogger(RetryExecutor.class);

    private static final int MAX_RETRIES = 3;        // retries AFTER first attempt
    private static final long BASE_DELAY_MS = 300;   // exponential backoff base

    private RetryExecutor() {}

    public static Response executeWithRetry(Supplier<Response> requestCall) {

        int attempt = 0;

        while (true) {
            try {

                Response response = requestCall.get();
                int status = response.statusCode();

                if (!shouldRetry(status) || attempt >= MAX_RETRIES) {
                    return response;
                }

                long delay = calculateDelay(attempt);
                log.warn("Retry attempt {} for status {}. Waiting {} ms",
                        attempt + 1, status, delay);

                sleep(delay);
                attempt++;

            } catch (Exception ex) {

                if (attempt >= MAX_RETRIES) {
                    throw ex;
                }

                long delay = calculateDelay(attempt);
                log.warn("Retry attempt {} due to exception: {}. Waiting {} ms",
                        attempt + 1, ex.getMessage(), delay);

                sleep(delay);
                attempt++;
            }
        }
    }

    private static boolean shouldRetry(int status) {
        return status >= 500 || status == 429 || status == 408;
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