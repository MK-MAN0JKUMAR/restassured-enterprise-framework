package framework.data.petstore;

import framework.data.petstore.builders.PetBuilder;
import framework.domain.petstore.PetRequest;

public final class PetstoreDataFactory {

    private PetstoreDataFactory() {}

    public static PetRequest validPet() {
        return new PetBuilder()
                .withDeterministicId()
                .withDefaultCategory()
                .withRandomName()
                .withPhotoUrls()
                .withNoTags()
                .withStatus("available")
                .build();
    }

    public static PetRequest soldPet() {
        return new PetBuilder()
                .withDeterministicId()
                .withDefaultCategory()
                .withRandomName()
                .withPhotoUrls()
                .withNoTags()
                .withStatus("sold")
                .build();
    }
}