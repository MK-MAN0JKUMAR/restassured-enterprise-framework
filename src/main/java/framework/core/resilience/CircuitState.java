package framework.core.resilience;

public enum CircuitState {
    CLOSED,
    OPEN,
    HALF_OPEN
}