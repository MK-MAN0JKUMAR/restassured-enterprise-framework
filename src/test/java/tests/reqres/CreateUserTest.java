package tests.reqres;

import framework.client.ReqresClient;
import framework.data.reqres.ReqresDataFactory;
import framework.models.reqres.CreateUserRequest;
import framework.models.reqres.CreateUserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class CreateUserTest extends BaseTest {

    private final ReqresClient reqresClient = new ReqresClient();

    @Test(groups = "smoke")
    public void shouldCreateUserSuccessfully() {

        // Arrange
        CreateUserRequest request = ReqresDataFactory.createRandomUser();

        // Act
        CreateUserResponse response =
                reqresClient
                        .createUser(request)
                        .as(CreateUserResponse.class);

        // Assert (business only)
        Assert.assertEquals(response.getName(), request.getName());
        Assert.assertEquals(response.getJob(), request.getJob());
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getCreatedAt());
    }
}
