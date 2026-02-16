package tests.reqres;

import framework.client.ReqresClient;
import framework.core.http.ResponseSpecFactory;
import framework.core.validation.ResponseValidator;
import framework.models.common.ErrorResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;
import tests.reqres.stubs.ErrorStubs;

public class CreateUserNegativeTest extends BaseTest {

    private final ReqresClient client = new ReqresClient();

    @Test(groups = {"negative", "regression"})
    public void shouldReturn400ForInvalidPayload() {

        ErrorStubs.stubCreateUserBadRequest();

        Response raw = client.createUser(new Object());

        // 1️⃣ Transport-level validation
        ResponseValidator.clientError(raw, 400);


        // 2️⃣ Payload-level validation
        ErrorResponse error = raw.as(ErrorResponse.class);

        Assert.assertEquals(error.getError(), "Bad Request");
        Assert.assertEquals(error.getStatus(), 400);
    }
}
