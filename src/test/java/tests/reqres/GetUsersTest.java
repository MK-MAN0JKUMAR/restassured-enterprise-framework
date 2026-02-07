package tests.reqres;

import framework.client.ReqresClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class GetUsersTest extends BaseTest {

    private final ReqresClient client = new ReqresClient();

    @Test(groups = "smoke")
    public void shouldFetchUsersList() {

        Response response = client.getUsersPage(2);

        Assert.assertEquals(response.jsonPath().getInt("page"), 2);
        Assert.assertTrue(response.jsonPath().getList("data").size() > 0);
    }
}
