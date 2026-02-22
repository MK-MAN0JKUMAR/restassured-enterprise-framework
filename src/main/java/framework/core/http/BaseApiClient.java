package framework.core.http;

import framework.constants.ServiceType;
import framework.core.reporting.AllureRestAssuredFilter;
import framework.core.retry.RetryExecutor;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    private static final Logger log =
            LogManager.getLogger(BaseApiClient.class);

    private final ServiceType serviceType;

    protected BaseApiClient(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    protected Response get(String path) {
        return execute(HttpMethod.GET, path, null, null, null, null);
    }

    protected Response delete(String path) {
        return execute(HttpMethod.DELETE, path, null, null, null, null);
    }

    protected Response post(String path, Object body) {
        return execute(HttpMethod.POST, path, body, null, null, null);
    }

    protected Response put(String path, Object body) {
        return execute(HttpMethod.PUT, path, body, null, null, null);
    }

    protected Response patch(String path, Object body) {
        return execute(HttpMethod.PATCH, path, body, null, null, null);
    }

    protected Response execute(HttpMethod method,
                               String path,
                               Object body,
                               Map<String, ?> pathParams,
                               Map<String, ?> queryParams,
                               File multipartFile) {

        RequestSpecification spec =
                RequestSpecFactory.get(serviceType);

        if (pathParams != null && !pathParams.isEmpty()) {
            spec.pathParams(pathParams);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }

        if (multipartFile != null) {
            spec.multiPart(multipartFile);
        } else if (body != null) {
            spec.contentType("application/json");
            spec.body(body);
        }

        long start = System.currentTimeMillis();

        Response response =
                RetryExecutor.executeWithRetry(method,
                        () -> sendRequest(spec, method, path));

        long duration = System.currentTimeMillis() - start;

        log.info("HTTP {} {} → {} ({} ms)",
                method, path, response.statusCode(), duration);

        return response;
    }

    private Response sendRequest(RequestSpecification spec,
                                 HttpMethod method,
                                 String path) {

        return switch (method) {
            case GET -> given()
                    .filter(new SensitiveHeaderFilter())
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when().get(path);

            case POST -> given()
                    .filter(new SensitiveHeaderFilter())
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when().post(path);

            case PUT -> given()
                    .filter(new SensitiveHeaderFilter())
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when().put(path);

            case DELETE -> given()
                    .filter(new SensitiveHeaderFilter())
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when().delete(path);

            case PATCH -> given()
                    .filter(new SensitiveHeaderFilter())
                    .filter(AllureRestAssuredFilter.get())
                    .spec(spec)
                    .when().patch(path);
        };
    }
}