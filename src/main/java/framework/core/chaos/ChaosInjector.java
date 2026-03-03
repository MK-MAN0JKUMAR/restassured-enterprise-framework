package framework.core.chaos;

import framework.constants.ServiceType;

import java.util.concurrent.ThreadLocalRandom;

public final class ChaosInjector {

    private ChaosInjector() {}

    public static void inject(ServiceType serviceType) {

        if (!ChaosConfig.isEnabled()) {
            return;
        }

        // Simulate latency
        long latency = ChaosConfig.latencyMs();
        if (latency > 0) {
            sleep(latency);
        }

        // Simulate failure
        double rate = ChaosConfig.failureRate();
        if (rate > 0) {

            double random = ThreadLocalRandom.current().nextDouble();

            if (random < rate) {
                throw new RuntimeException(
                        "Chaos injected failure for service: " + serviceType);
            }
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}