package framework.core.resilience;

import framework.constants.ServiceType;

import java.util.concurrent.ConcurrentHashMap;

public final class CircuitBreakerRegistry {

    private static final ConcurrentHashMap<ServiceType, CircuitBreaker> REGISTRY =
            new ConcurrentHashMap<>();

    private CircuitBreakerRegistry() {}

    public static CircuitBreaker get(ServiceType service) {

        return REGISTRY.computeIfAbsent(service,
                s -> new CircuitBreaker(3, 5000)); // 3 failures, 5 sec timeout
    }
}