package framework.domain.petstore;

import java.util.List;

public class PetResponse {

    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Object> tags;
    private String status;

    public PetResponse() {
        // Required for Jackson deserialization
    }

    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public List<Object> getTags() {
        return tags;
    }

    public String getStatus() {
        return status;
    }
}