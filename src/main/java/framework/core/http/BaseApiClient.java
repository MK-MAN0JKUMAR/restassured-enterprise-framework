package framework.core.http;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected Response get(String path) {
        return execute(RequestSpecFactory.get(), "GET", path);
    }

    protected Response delete(String path) {
        return execute(RequestSpecFactory.get(), "DELETE", path);
    }

    protected Response post(String path, Object body) {
        RequestSpecification spec = RequestSpecFactory.get().body(body);
        return execute(spec, "POST", path);
    }

    protected Response put(String path, Object body) {
        RequestSpecification spec = RequestSpecFactory.get().body(body);
        return execute(spec, "PUT", path);
    }

    private Response execute(RequestSpecification spec, String method, String path) {

        return switch (method) {
            case "GET" -> given().spec(spec).when().get(path);
            case "POST" -> given().spec(spec).when().post(path);
            case "PUT" -> given().spec(spec).when().put(path);
            case "DELETE" -> given().spec(spec).when().delete(path);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }
}
