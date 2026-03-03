package framework.core.observability;

import java.util.UUID;

public final class CorrelationManager {

    private static final ThreadLocal<String> CORRELATION_ID =
            ThreadLocal.withInitial(() ->
                    UUID.randomUUID().toString()
            );

    private CorrelationManager() {}

    public static String getId() {
        return CORRELATION_ID.get();
    }

    public static void reset() {
        CORRELATION_ID.remove();
    }
}