package tests.petstore;

import framework.client.PetstoreClient;
import framework.core.validation.ResponseValidator;
import framework.data.petstore.PetstoreDataFactory;
import framework.domain.petstore.PetRequest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.io.File;

@Test(groups = {"petstore", "regression"})
public class UploadPetImageTest extends BaseTest {

    private final PetstoreClient client = new PetstoreClient();

    @Test
    public void shouldUploadPetImageSuccessfully() {

        // Create pet first
        PetRequest request = PetstoreDataFactory.validPet();
        Response createResponse = client.createPet(request);

        ResponseValidator.success(createResponse);

        long petId = createResponse.jsonPath().getLong("id");

        // Upload file
        File file = new File("src/test/resources/sample-image.jpg");

        Response uploadResponse = client.uploadImage(petId, file);

        ResponseValidator.success(uploadResponse);
    }
}