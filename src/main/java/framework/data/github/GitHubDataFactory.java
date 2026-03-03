package framework.data.github;

import framework.data.github.builders.RepoBuilder;
import framework.domain.github.CreateRepoRequest;

public final class GitHubDataFactory {

    private GitHubDataFactory() {}

    public static CreateRepoRequest createPublicRepo() {

        return RepoBuilder.create()
                .withRandomName()
                .asPublic()
                .build();
    }

    public static CreateRepoRequest createPrivateRepo() {

        return RepoBuilder.create()
                .withRandomName()
                .asPrivate()
                .build();
    }

    public static CreateRepoRequest createRepoWithCustomName(String name) {

        return RepoBuilder.create()
                .withName(name)
                .asPublic()
                .build();
    }
}