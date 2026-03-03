package framework.core.http;

import framework.core.config.FrameworkConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public final class ResponseSpecFactory {

    private ResponseSpecFactory() {}

    // ========================= SUCCESS =========================

    /**
     * 200 OK with JSON enforcement
     */
    public static ResponseSpecification successJson() {
        return baseSpec()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * 201 Created with JSON enforcement
     */
    public static ResponseSpecification createdJson() {
        return baseSpec()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * 204 No Content (nobody expected)
     */
    public static ResponseSpecification successNoContent() {
        return baseSpec()
                .expectStatusCode(204)
                .build();
    }

    // ========================= STATUS ONLY =========================

    /**
     * Custom status with JSON enforcement
     */
    public static ResponseSpecification statusWithJson(int expectedStatus) {
        return baseSpec()
                .expectStatusCode(expectedStatus)
                .expectContentType(ContentType.JSON)
                .build();
    }

    /**
     * Custom status without JSON enforcement
     */
    public static ResponseSpecification statusWithoutJson(int expectedStatus) {
        return baseSpec()
                .expectStatusCode(expectedStatus)
                .build();
    }

    // ========================= CLIENT ERROR =========================

    /**
     * 4xx validation with JSON error body
     */
    public static ResponseSpecification clientError(int expectedStatus) {
        return baseSpec()
                .expectStatusCode(expectedStatus)
                .expectContentType(ContentType.JSON)
                .build();
    }

    // ========================= BASE SPEC (SLA CONTROL) =========================

    /**
     * Central SLA enforcement.
     * IMPORTANT: SLA must NOT reuse read timeout.
     */
    private static ResponseSpecBuilder baseSpec() {

        FrameworkConfig config = FrameworkConfig.get();

        long slaMs = resolveSla(config);

        return new ResponseSpecBuilder()
                .expectResponseTime(lessThan(slaMs), TimeUnit.MILLISECONDS);
    }

    /**
     * Temporary SLA resolution.
     * TODO: Move to dedicated config property (api.sla.ms).
     */
    private static long resolveSla(FrameworkConfig config) {

        String cliSla = System.getProperty("api.sla.ms");

        if (cliSla != null) {
            return Long.parseLong(cliSla);
        }
        return Long.parseLong(config.getOptional("api.sla.ms", "5000"));
    }
}