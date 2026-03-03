package framework.core.retry;

public final class RetryContext {

    private static final ThreadLocal<Integer> ATTEMPTS =
            ThreadLocal.withInitial(() -> 0);

    private RetryContext() {}

    static void increment() {
        ATTEMPTS.set(ATTEMPTS.get() + 1);
    }

    static void reset() {
        ATTEMPTS.set(0);
    }

    public static int getAttempts() {
        return ATTEMPTS.get();
    }
}