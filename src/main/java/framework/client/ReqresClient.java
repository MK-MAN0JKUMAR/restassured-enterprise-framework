package framework.client;

import framework.constants.ReqresEndpoints;
import framework.constants.ServiceType;
import framework.core.http.BaseApiClient;
import framework.core.mock.WireMockManager;
import io.restassured.response.Response;

public class ReqresClient extends BaseApiClient {

    public ReqresClient() {
        super(ServiceType.REQRES);
    }

    @Override
    protected Response get(String path) {

        if (WireMockManager.isRunning()) {
            return executeWithOverrideBaseUrl(path);
        }

        return super.get(path);
    }

    private Response executeWithOverrideBaseUrl(String path) {
        return super.execute(
                framework.core.http.HttpMethod.GET,
                path,
                null,
                null,
                null,
                null
        );
    }

    public Response getUsers() {
        return get(ReqresEndpoints.USERS);
    }

    public Response getUserById(int userId) {
        return get(String.format(ReqresEndpoints.SINGLE_USER, userId));
    }

    public Response createUser(Object body) {
        return post(ReqresEndpoints.USERS, body);
    }

    public Response updateUser(int userId, Object body) {
        return put(String.format(ReqresEndpoints.SINGLE_USER, userId), body);
    }

    public Response deleteUser(int userId) {
        return delete(String.format(ReqresEndpoints.SINGLE_USER, userId));
    }

    public Response getUsersPage(int page) {
        return get(String.format(ReqresEndpoints.USERS_PAGE, page));
    }

    public Response retryTestEndpoint() {
        return get("/retry-test");
    }
}