package tests.petstore;

import framework.client.PetstoreClient;
import framework.core.validation.ResponseValidator;
import framework.data.petstore.PetstoreDataFactory;
import framework.domain.petstore.PetRequest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class CreatePetTest extends BaseTest {

    private final PetstoreClient client = new PetstoreClient();

    @Test
    public void shouldCreatePetSuccessfully() {

        PetRequest pet = PetstoreDataFactory.validPet();

        Response response = client.createPet(pet);

        ResponseValidator.successWithSchema(
                response,
                "petstore/create-pet-response.json"
        );

        ResponseValidator.assertResponseTime(response, 3000);
    }
}