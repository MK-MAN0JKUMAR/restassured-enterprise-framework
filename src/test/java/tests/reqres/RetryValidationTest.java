package tests.reqres;

import framework.client.ReqresClient;
import framework.core.retry.RetryContext;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.reqres.stubs.RetrySimulationStub;

public class RetryValidationTest extends ReqresBaseTest {

    private final ReqresClient client = new ReqresClient();

    @Test(groups = {"retry"})
    public void shouldRetryGetOnServerError() {

        // Stub 500 -> 500 -> 200
        RetrySimulationStub.stub500Then200();

        Response response = client.retryTestEndpoint();

        Assert.assertEquals(response.statusCode(), 200);

        // ---> Validate retry attempts (professional way)
        Assert.assertTrue(RetryContext.getAttempts() >= 2);
    }
}