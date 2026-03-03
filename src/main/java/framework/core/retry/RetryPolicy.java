package framework.core.retry;

import framework.core.http.HttpMethod;

import java.util.Set;

public class RetryPolicy {

    private final int maxAttempts;
    private final long delayMillis;
    private final Set<Integer> retryableStatusCodes;

    public RetryPolicy(int maxAttempts,
                       long delayMillis,
                       Set<Integer> retryableStatusCodes) {
        this.maxAttempts = maxAttempts;
        this.delayMillis = delayMillis;
        this.retryableStatusCodes = retryableStatusCodes;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public boolean shouldRetry(HttpMethod method, int statusCode) {
        if (method != HttpMethod.GET) {
            return false;
        }

        return retryableStatusCodes.contains(statusCode);
    }
}