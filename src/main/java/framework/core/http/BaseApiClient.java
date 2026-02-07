package framework.core.http;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    protected Response get(String path) {
        return execute(RequestSpecFactory.get(), "GET", path, null);
    }

    protected Response delete(String path) {
        return execute(RequestSpecFactory.get(), "DELETE", path, null);
    }

    protected Response post(String path, Object body) {
        RequestSpecification spec = RequestSpecFactory.get().body(body);
        return execute(spec, "POST", path, null);
    }

    protected Response put(String path, Object body) {
        RequestSpecification spec = RequestSpecFactory.get().body(body);
        return execute(spec, "PUT", path, null);
    }

    private Response execute(RequestSpecification spec,
                             String method,
                             String path,
                             Object unused) {

        Response response;

        switch (method) {

            case "GET":
                response = given().spec(spec).when().get(path);
                break;

            case "POST":
                response = given().spec(spec).when().post(path);
                break;

            case "PUT":
                response = given().spec(spec).when().put(path);
                break;

            case "DELETE":
                response = given().spec(spec).when().delete(path);
                break;

            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // Centralized success validation
        response.then().spec(ResponseSpecFactory.success());

        return response;
    }
}
