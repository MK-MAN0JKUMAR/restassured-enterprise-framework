package framework.data.github.builders;

import framework.data.DataContext;
import framework.domain.github.CreateRepoRequest;

public class RepoBuilder {

    private String name;
    private String description = "Created by RestAssured Enterprise Framework";
    private boolean isPrivate = false;

    private RepoBuilder() {
        // enforce use of factory method
    }

    public static RepoBuilder create() {
        return new RepoBuilder();
    }

    public RepoBuilder withRandomName() {
        this.name = generateDeterministicName("repo");
        return this;
    }

    public RepoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RepoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public RepoBuilder asPrivate() {
        this.isPrivate = true;
        return this;
    }

    public RepoBuilder asPublic() {
        this.isPrivate = false;
        return this;
    }

    public CreateRepoRequest build() {

        if (name == null || name.isBlank()) {
            throw new IllegalStateException(
                    "Repository name must be set before build()");
        }

        return new CreateRepoRequest(
                name,
                description,
                isPrivate
        );
    }

    private String generateDeterministicName(String prefix) {
        return prefix + "-" + DataContext.get().uniqueSuffix();
    }
}