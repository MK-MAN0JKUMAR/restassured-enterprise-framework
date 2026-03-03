package framework.data;

import java.time.Instant;

public final class DataContext {

    private static final ThreadLocal<DataContext> CONTEXT =
            ThreadLocal.withInitial(DataContext::new);

    private final long threadId;
    private final long seed;
    private final long timestamp;

    private DataContext() {
        this.threadId = Thread.currentThread().getId();
        this.seed = DataSeedManager.getSeed();
        this.timestamp = Instant.now().toEpochMilli();
    }

    public static DataContext get() {
        return CONTEXT.get();
    }

    public String uniqueSuffix() {
        return seed + "_" + threadId;
    }

    // ---------------- CI SAFE UNIQUE SUFFIX ----------------
    public String ciSafeSuffix() {
        String runId = System.getenv("GITHUB_RUN_ID");

        if (runId == null || runId.isBlank()) {
            runId = String.valueOf(timestamp);
        }

        return uniqueSuffix() + "_" + runId;
    }
}