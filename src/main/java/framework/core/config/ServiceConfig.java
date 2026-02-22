package framework.core.config;

public final class ServiceConfig {

    private final String baseUrl;
    private final String authType;
    private final String token;

    public ServiceConfig(String baseUrl, String authType, String token) {
        this.baseUrl = baseUrl;
        this.authType = authType;
        this.token = token;
    }

    public String getBaseUrl() { return baseUrl; }

    public String getAuthType() { return authType; }

    public String getToken() { return token; }
}