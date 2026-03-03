package tests.petstore;

import framework.client.PetstoreClient;
import framework.core.validation.ResponseValidator;
import framework.data.petstore.PetstoreDataFactory;
import framework.domain.petstore.PetRequest;
import framework.domain.petstore.PetResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class CreateGetDeletePetFlowTest extends BaseTest {

    private final PetstoreClient client = new PetstoreClient();

    @Test(groups = {"petstore", "regression"})
    public void shouldCompletePetLifecycle() {

        // =========================
        // 1. Create
        // =========================

        PetRequest request = PetstoreDataFactory.validPet();

        Response createResponse = client.createPet(request);

        ResponseValidator.successWithSchema(
                createResponse,
                "petstore/create-pet-response.json"
        );

        PetResponse createdPet = createResponse.as(PetResponse.class);
        long petId = createdPet.getId();

        // =========================
        // 2. Get
        // =========================

        Response getResponse = client.getPetById(petId);

        ResponseValidator.successWithSchema(
                getResponse,
                "petstore/get-pet-response.json"
        );

        PetResponse fetchedPet = getResponse.as(PetResponse.class);

        assert fetchedPet.getId() == petId;
        assert fetchedPet.getName().equals(request.getName());

        // =========================
        // 3. Delete
        // =========================

        Response deleteResponse = client.deletePet(petId);

        ResponseValidator.success(deleteResponse);

        // =========================
        // 4. Verify Not Found
        // =========================

        Response notFoundResponse = client.getPetById(petId);

        ResponseValidator.clientError(notFoundResponse, 404);    }
}