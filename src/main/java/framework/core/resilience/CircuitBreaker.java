package framework.core.resilience;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreaker {

    private final int failureThreshold;
    private final long openTimeoutMs;

    @Getter
    private volatile CircuitState state = CircuitState.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);

    private volatile long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, long openTimeoutMs) {
        this.failureThreshold = failureThreshold;
        this.openTimeoutMs = openTimeoutMs;
    }

    public synchronized boolean allowRequest() {

        if (state == CircuitState.OPEN) {

            long now = System.currentTimeMillis();

            if (now - lastFailureTime >= openTimeoutMs) {
                state = CircuitState.HALF_OPEN;
                return true;
            }

            return false;
        }

        return true;
    }

    public synchronized void recordSuccess() {

        failureCount.set(0);
        state = CircuitState.CLOSED;
    }

    public synchronized void recordFailure() {

        int failures = failureCount.incrementAndGet();
        lastFailureTime = System.currentTimeMillis();

        if (failures >= failureThreshold) {
            state = CircuitState.OPEN;
        }
    }

}