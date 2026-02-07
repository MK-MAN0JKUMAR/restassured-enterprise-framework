package framework.core.http;

import framework.core.config.FrameworkConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public final class ResponseSpecFactory {

    private static final ResponseSpecification SUCCESS_SPEC = buildSuccessSpec();

    private ResponseSpecFactory() {}

    /**
     * Standard success validation:
     * - Status 2xx
     * - JSON response
     * - Response time within SLA
     */
    public static ResponseSpecification success() {
        return SUCCESS_SPEC;
    }

    /**
     * Custom expected status with SLA + JSON enforcement.
     */
    public static ResponseSpecification status(int expectedStatus) {
        FrameworkConfig config = FrameworkConfig.get();

        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatus)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(
                        lessThan((long) config.getReadTimeout()),
                        TimeUnit.MILLISECONDS
                )
                .build();
    }

    private static ResponseSpecification buildSuccessSpec() {

        FrameworkConfig config = FrameworkConfig.get();

        return new ResponseSpecBuilder()
                .expectStatusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(200),
                        org.hamcrest.Matchers.is(201),
                        org.hamcrest.Matchers.is(202),
                        org.hamcrest.Matchers.is(204)
                ))
                .expectContentType(ContentType.JSON)
                .expectResponseTime(
                        lessThan((long) config.getReadTimeout()),
                        TimeUnit.MILLISECONDS
                )
                .build();
    }
}
