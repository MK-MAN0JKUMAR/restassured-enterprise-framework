package framework.client;

import framework.constants.ReqresEndpoints;
import framework.core.http.BaseApiClient;
import io.restassured.response.Response;

public class ReqresClient extends BaseApiClient {

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

    public Response getUsersPage(int userId) {
        return get(String.format(ReqresEndpoints.USERS_PAGE, userId));
    }

}
