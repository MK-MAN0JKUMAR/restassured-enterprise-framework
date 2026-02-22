package framework.data;

import java.util.Random;

public final class DataSeedManager {

    private static final long DEFAULT_SEED = 123456L;

    private static final long GLOBAL_SEED =
            Long.parseLong(System.getProperty("data.seed",
                    String.valueOf(DEFAULT_SEED)));

    private static final ThreadLocal<Random> THREAD_RANDOM =
            ThreadLocal.withInitial(() ->
                    new Random(GLOBAL_SEED + Thread.currentThread().getId()));

    private DataSeedManager() {}

    public static Random random() {
        return THREAD_RANDOM.get();
    }

    public static long getSeed() {
        return GLOBAL_SEED;
    }

    public static long nextId() {
        long value = Math.abs(random().nextLong());
        return value == 0 ? 1 : value;
    }

}