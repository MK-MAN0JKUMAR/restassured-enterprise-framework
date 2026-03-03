package tests.reqres;

import framework.client.ReqresClient;
import framework.core.validation.ResponseValidator;
import framework.data.reqres.ReqresDataFactory;
import framework.domain.reqres.CreateUserRequest;
import framework.domain.reqres.CreateUserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;
import tests.reqres.stubs.CreateUserStub;

public class CreateUserTest extends ReqresBaseTest {

    private final ReqresClient reqresClient = new ReqresClient();

    @Test(groups = {"reqres", "smoke", "regression"})
    public void shouldCreateUserSuccessfully() {

        // Arrange
        CreateUserStub.stubCreateUser();
        CreateUserRequest request = ReqresDataFactory.createRandomUser();

        // Act  (ONLY ONE CALL)
        Response raw = reqresClient.createUser(request);

        ResponseValidator.successWithSchema(
                raw,
                "reqres/create-user-response.json"
        );

        CreateUserResponse response = raw.as(CreateUserResponse.class);

        // Assert (contract-level for mock)
        Assert.assertEquals(response.getJob(), "leader");
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getCreatedAt());

    }
}
