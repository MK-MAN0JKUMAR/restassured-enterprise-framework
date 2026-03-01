package framework.core.metrics;

import framework.constants.ServiceType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class MetricsCollector {

    private static final Map<ServiceType, List<Long>> METRICS =
            new ConcurrentHashMap<>();

    private MetricsCollector() {}

    public static void record(ServiceType service, long duration) {

        METRICS
                .computeIfAbsent(service, k -> new CopyOnWriteArrayList<>())
                .add(duration);
    }

    public static Map<ServiceType, List<Long>> getAll() {
        return Collections.unmodifiableMap(METRICS);
    }

    public static void reset() {
        METRICS.clear();
    }
}