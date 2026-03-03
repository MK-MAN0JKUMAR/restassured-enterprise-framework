package tests.petstore;

import framework.client.PetstoreClient;
import framework.core.validation.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;

@Test(groups = {"petstore", "regression"})
public class FindPetByStatusTest extends BaseTest {

    private final PetstoreClient client = new PetstoreClient();

    @Test
    public void shouldFindPetsByStatus() {

        Response response = client.findByStatus("available");

        ResponseValidator.success(response);

        ResponseValidator.schema(
                response,
                "petstore/find-by-status-response.json"
        );
    }
}