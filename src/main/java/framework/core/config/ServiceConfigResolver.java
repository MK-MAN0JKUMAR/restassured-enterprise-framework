package framework.core.config;

import framework.constants.ServiceType;
import framework.core.exception.ConfigException;
import framework.core.mock.WireMockManager;

public final class ServiceConfigResolver {

    private ServiceConfigResolver() {}

    public static ServiceConfig resolve(ServiceType type) {

        FrameworkConfig config = FrameworkConfig.get();
        String prefix = type.name().toLowerCase();

        String baseUrl = config.getRequired(prefix + ".base.url");

        // Mock override for Reqres only
        if (type == ServiceType.REQRES && WireMockManager.isRunning()) {
            baseUrl = WireMockManager.baseUrl();
        }

        String authType = config.getOptional(prefix + ".auth.type", "none");
        String token = config.getTokenOverride(prefix + ".token");

        if ("bearer".equalsIgnoreCase(authType) && token.isBlank()) {
            throw new ConfigException("Token required for service: " + type);
        }

        return new ServiceConfig(baseUrl, authType, token);
    }
}