package framework.core.http;

import framework.constants.ServiceType;
import framework.core.config.FrameworkConfig;
import framework.core.config.ServiceConfig;
import framework.core.config.ServiceConfigResolver;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private RequestSpecFactory() {}

    /**
     * Backward compatibility (default = REQRES).
     */
    public static RequestSpecification get() {
        return get(ServiceType.REQRES);
    }

    public static RequestSpecification get(ServiceType serviceType) {

        ServiceConfig serviceConfig =
                ServiceConfigResolver.resolve(serviceType);

        FrameworkConfig globalConfig =
                FrameworkConfig.get();

        RestAssuredConfig raConfig = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout",
                                globalConfig.getInt("connect.timeout"))
                        .setParam("http.socket.timeout",
                                globalConfig.getInt("read.timeout"))
                );

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(serviceConfig.getBaseUrl())
                .setAccept(ContentType.JSON)
//                .setContentType(ContentType.JSON)   // safe default for APIs
                .setConfig(raConfig)
                .addHeader("User-Agent", "RestAssured-Enterprise-Framework")
                .addHeader("X-Correlation-Id", framework.core.observability.CorrelationManager.getId())
                .log(LogDetail.URI)
                .log(LogDetail.METHOD);

        applyAuth(builder, serviceConfig);

        return builder.build();
    }

    private static void applyAuth(RequestSpecBuilder builder,
                                  ServiceConfig serviceConfig) {

        if ("bearer".equalsIgnoreCase(serviceConfig.getAuthType())
                && !serviceConfig.getToken().isBlank()) {

            builder.addHeader("Authorization",
                    "Bearer " + serviceConfig.getToken());

            // Apply masking filter ONLY for bearer auth
            builder.addFilter(new SensitiveHeaderFilter());
        }
    }
}