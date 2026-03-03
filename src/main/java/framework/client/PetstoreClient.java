package framework.client;

import framework.constants.PetstoreEndpoints;
import framework.constants.ServiceType;
import framework.core.http.BaseApiClient;
import framework.core.http.HttpMethod;
import framework.domain.petstore.PetRequest;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

public class PetstoreClient extends BaseApiClient {

    public PetstoreClient() {
        super(ServiceType.PETSTORE);
    }

    public Response createPet(PetRequest request) {
        return post(PetstoreEndpoints.PET, request);
    }

    public Response getPetById(long petId) {
        return execute(
                HttpMethod.GET,
                PetstoreEndpoints.PET_BY_ID,
                null,
                Map.of("petId", petId),
                null,
                null
        );
    }

    public Response deletePet(long petId) {
        return execute(
                HttpMethod.DELETE,
                PetstoreEndpoints.PET_BY_ID,
                null,
                Map.of("petId", petId),
                null,
                null
        );
    }

    public Response findByStatus(String status) {
        return execute(
                HttpMethod.GET,
                PetstoreEndpoints.FIND_BY_STATUS,
                null,
                null,
                Map.of("status", status),
                null
        );
    }

    public Response uploadImage(long petId, File file) {
        return execute(
                HttpMethod.POST,
                PetstoreEndpoints.UPLOAD_IMAGE,
                null,
                Map.of("petId", petId),
                null,
                file
        );
    }
}