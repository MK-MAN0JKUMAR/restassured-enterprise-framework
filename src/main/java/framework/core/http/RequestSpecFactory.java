package framework.core.http;

import framework.core.config.FrameworkConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private static final RequestSpecification BASE_SPEC = buildBaseSpec();

    private RequestSpecFactory() {}

    /**
     * Returns a fresh RequestSpecification cloned from base.
     * Prevents mutation side effects in parallel execution.
     */
    public static RequestSpecification get() {
        return new RequestSpecBuilder()
                .addRequestSpecification(BASE_SPEC)
                .build();
    }

    private static RequestSpecification buildBaseSpec() {

        FrameworkConfig config = FrameworkConfig.get();

        RestAssuredConfig raConfig = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getConnectTimeout())
                        .setParam("http.socket.timeout", config.getReadTimeout())
                );

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(raConfig)

                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120 Safari/537.36")
                .addHeader("Accept-Language", "en-US,en;q=0.9")


                .log(LogDetail.URI)
                .log(LogDetail.METHOD);

        applyAuth(builder, config);

        return builder.build();
    }

    private static void applyAuth(RequestSpecBuilder builder, FrameworkConfig config) {

        switch (config.getAuthType().toLowerCase()) {

            case "bearer":
                if (!config.getToken().isBlank()) {
                    builder.addHeader("Authorization", "Bearer " + config.getToken());
                }
                break;

            case "none":
            default:
                // intentionally empty
        }
    }
}
