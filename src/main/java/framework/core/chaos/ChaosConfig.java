package framework.core.chaos;

public final class ChaosConfig {

    private static final boolean ENABLED = Boolean.parseBoolean(System.getProperty("chaos.enabled", "false"));

    private static final double FAILURE_RATE = Double.parseDouble(System.getProperty("chaos.failure.rate", "0.0"));

    private static final long LATENCY_MS = Long.parseLong(System.getProperty("chaos.latency.ms", "0"));

    private ChaosConfig() {}

    public static boolean isEnabled() {
        return ENABLED;
    }

    public static double failureRate() {
        return FAILURE_RATE;
    }

    public static long latencyMs() {
        return LATENCY_MS;
    }
}