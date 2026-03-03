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
        String token = resolveToken(type, authType, prefix);

        return new ServiceConfig(baseUrl, authType, token);
    }

    private static String resolveToken(ServiceType type, String authType, String prefix) {

        if (!"bearer".equalsIgnoreCase(authType)) {
            return "";
        }

        // Strict enforcement for GitHub
        if (type == ServiceType.GITHUB) {

            String tokenFromSysProp = System.getProperty(prefix + ".token");
            String tokenFromEnv = System.getenv("GITHUB_TOKEN");

            String token = firstNonBlank(tokenFromSysProp, tokenFromEnv);

            if (token == null || token.isBlank()) {
                throw new ConfigException(
                        "GitHub token must be provided via -Dgithub.token or GITHUB_TOKEN environment variable"
                );
            }

            return token;
        }

        // For other services (if bearer ever used)
        String token = System.getProperty(prefix + ".token");
        if (token == null || token.isBlank()) {
            throw new ConfigException("Token required for service: " + type);
        }

        return token;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }
}