package tests.reqres;

import framework.client.ReqresClient;
import framework.core.schema.JsonSchemaValidator;
import framework.models.reqres.GetUsersResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;
import tests.reqres.stubs.GetUsersStub;

public class GetUsersTest extends BaseTest {

    private final ReqresClient client = new ReqresClient();

    @Test(groups = {"smoke", "regression"})
    public void shouldFetchUsersList() {

        GetUsersStub.stubUsersPage2();

        Response raw = client.getUsersPage(2);
        JsonSchemaValidator.validate(raw, "reqres/get-users-response.json");

        // Optional debug (safe)
        System.out.println("CreateUser response = " + raw.asString());

        GetUsersResponse response = raw.as(GetUsersResponse.class);

        Assert.assertEquals(response.getPage(), 2);
        Assert.assertFalse(response.getData().isEmpty());
    }
}
