package tests.github;

import framework.constants.ServiceType;
import framework.core.annotation.Service;
import framework.core.http.RequestSpecFactory;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import static io.restassured.RestAssured.given;

public class GitHubAuthNegativeTest extends BaseTest {

    @Service("github")
    @Test(groups = {"negative", "regression"})
    public void shouldReturn401ForInvalidToken() {

        Response response = given()
                .spec(RequestSpecFactory.get(ServiceType.GITHUB))
                .header("Authorization", "Bearer invalid_token")
                .when()
                .get("/user");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertTrue(response.asString().contains("Bad credentials"));
        Assert.assertTrue(response.asString().contains("documentation_url"));
    }
}