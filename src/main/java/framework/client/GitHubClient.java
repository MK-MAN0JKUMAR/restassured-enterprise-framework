package framework.client;

import framework.constants.ServiceType;
import framework.core.http.BaseApiClient;
import framework.domain.github.CreateRepoRequest;
import io.restassured.response.Response;

public class GitHubClient extends BaseApiClient {

    public GitHubClient() {
        super(ServiceType.GITHUB);
    }

    /**
     * Returns raw Response.
     * Test layer must assert status code before mapping.
     */
    public Response createRepo(CreateRepoRequest request) {
        return post("/user/repos", request);
    }

    public Response getRepo(String owner, String repoName) {
        return get("/repos/" + owner + "/" + repoName);
    }

    public Response deleteRepo(String owner, String repoName) {
        return delete("/repos/" + owner + "/" + repoName);
    }
}