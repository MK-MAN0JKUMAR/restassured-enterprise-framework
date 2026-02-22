package framework.data.petstore.builders;

import framework.data.DataContext;
import framework.data.DataSeedManager;
import framework.domain.petstore.Category;
import framework.domain.petstore.PetRequest;

import java.util.List;

public class PetBuilder {

    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Object> tags;
    private String status;

    public PetBuilder withDeterministicId() {
        this.id = DataSeedManager.nextId();
        return this;
    }

    public PetBuilder withDefaultCategory() {
        this.category = new Category(1L, "dogs");
        return this;
    }

    public PetBuilder withRandomName() {
        this.name = "pet_" + DataContext.get().uniqueSuffix();
        return this;
    }

    public PetBuilder withPhotoUrls() {
        this.photoUrls = List.of("https://example.com/photo1.jpg");
        return this;
    }

    public PetBuilder withNoTags() {
        this.tags = List.of();
        return this;
    }

    public PetBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public PetRequest build() {
        return new PetRequest(
                id,
                category,
                name,
                photoUrls,
                tags,
                status
        );
    }
}