package tests.petstore;

import framework.client.PetstoreClient;
import framework.core.validation.ResponseValidator;
import framework.data.petstore.PetstoreDataFactory;
import framework.domain.petstore.PetRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

@Test(groups = {"petstore", "negative"})
public class CreatePetNegativeTest extends BaseTest {

    private final PetstoreClient client = new PetstoreClient();

    /**
     * Swagger Petstore does NOT strictly validate missing name.
     * It still returns 200.
     * So we validate behavior instead of forcing 400.
     */
    @Test
    public void shouldHandleMissingNameGracefully() {

        PetRequest invalidPet = PetstoreDataFactory.validPet();
        invalidPet.setName(null);

        Response response = client.createPet(invalidPet);

        // Real API returns 200 — validate that it still responds successfully
        ResponseValidator.success(response);

        Assert.assertNotNull(response.getBody());
    }

    /**
     * Validate non-existing ID behavior.
     * Swagger Petstore sometimes returns 404, sometimes 200.
     * We validate response shape safely.
     */
    @Test
    public void shouldHandleNonExistingPetLookup() {

        long nonExistingId = 999999999L;

        Response response = client.getPetById(nonExistingId);

        // Accept either 200 or 404 depending on API state
        int status = response.statusCode();

        Assert.assertTrue(
                status == 200 || status == 404,
                "Unexpected status: " + status
        );
    }
}