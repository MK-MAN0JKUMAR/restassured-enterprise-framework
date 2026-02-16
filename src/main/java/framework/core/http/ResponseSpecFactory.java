package framework.core.http;

import framework.core.config.FrameworkConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public final class ResponseSpecFactory {

    private ResponseSpecFactory() {}

    /**
     * Standard success validation:
     * - Status 2xx
     * - Optional JSON enforcement
     * - SLA bound (separate from timeout)
     */
    public static ResponseSpecification success(boolean enforceJson) {

        FrameworkConfig config = FrameworkConfig.get();
        long sla = config.getReadTimeout(); // later replace with dedicated SLA config

        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(200),
                        org.hamcrest.Matchers.is(201),
                        org.hamcrest.Matchers.is(202),
                        org.hamcrest.Matchers.is(204)
                ))
                .expectResponseTime(lessThan(sla), TimeUnit.MILLISECONDS);

        if (enforceJson) {
            builder.expectContentType(ContentType.JSON);
        }

        return builder.build();
    }

    /**
     * Custom expected status with optional JSON + SLA.
     */
    public static ResponseSpecification status(int expectedStatus, boolean enforceJson) {

        FrameworkConfig config = FrameworkConfig.get();
        long sla = config.getReadTimeout();

        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(expectedStatus)
                .expectResponseTime(lessThan(sla), TimeUnit.MILLISECONDS);

        if (enforceJson) {
            builder.expectContentType(ContentType.JSON);
        }

        return builder.build();
    }

    /**
     * Client error validation (4xx)
     */
    public static ResponseSpecification clientError(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
