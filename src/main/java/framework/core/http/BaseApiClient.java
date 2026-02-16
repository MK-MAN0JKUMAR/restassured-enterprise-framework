package framework.core.http;

import framework.core.reporting.AllureRestAssuredFilter;
import framework.core.retry.RetryExecutor;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    private static final Logger log = LogManager.getLogger(BaseApiClient.class);

    protected Response get(String path) {
        return execute(HttpMethod.GET, path, null);
    }

    protected Response delete(String path) {
        return execute(HttpMethod.DELETE, path, null);
    }

    protected Response post(String path, Object body) {
        return execute(HttpMethod.POST, path, body);
    }

    protected Response put(String path, Object body) {
        return execute(HttpMethod.PUT, path, body);
    }

    protected Response patch(String path, Object body) {
        return execute(HttpMethod.PATCH, path, body);
    }

    /**
     * Single enterprise execution pipeline.
     */
    private Response execute(HttpMethod method, String path, Object body) {

        RequestSpecification spec = RequestSpecFactory.get();

        if (body != null) {
            spec.body(body);
        }

        long start = System.currentTimeMillis();

        Response response = RetryExecutor.executeWithRetry(() ->
                sendRequestWithAllure(spec, method, path)
        );

        long duration = System.currentTimeMillis() - start;

        log.info("HTTP {} {} → {} ({} ms)",
                method, path, response.statusCode(), duration);

        return response;
    }

    /**
     * All requests MUST pass through this method.
     * Ensures:
     *  - Allure capture
     *  - Centralized sending logic
     */
    private Response sendRequestWithAllure(RequestSpecification spec, HttpMethod method, String path) {

        return switch (method) {
            case GET -> given()
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when()
                    .get(path);

            case POST -> given()
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when()
                    .post(path);

            case PUT -> given()
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when()
                    .put(path);

            case DELETE -> given()
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when()
                    .delete(path);

            case PATCH -> given()
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when()
                    .patch(path);
        };
    }
}
