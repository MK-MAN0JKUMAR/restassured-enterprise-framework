package tests.github;

import framework.client.GitHubClient;
import framework.data.github.GitHubDataFactory;
import framework.domain.github.CreateRepoRequest;
import framework.domain.github.RepoResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class GitHubRepoSmokeTest extends BaseTest {

    private final GitHubClient gitHubClient = new GitHubClient();
    private String createdRepoName;

    private final String owner = System.getenv("GITHUB_USERNAME");

    @Test(groups = {"github", "smoke"})
    public void shouldCreateAndDeleteRepository() {

        Assert.assertNotNull(owner,
                "GITHUB_USERNAME environment variable must be set");

        CreateRepoRequest request =
                GitHubDataFactory.createPublicRepo();

        createdRepoName = request.getName();

        Response raw =
                gitHubClient.createRepo(request);

        Assert.assertEquals(raw.statusCode(), 201,
                "Repository creation failed: " + raw.asString());

        RepoResponse response =
                raw.as(RepoResponse.class);

        Assert.assertEquals(response.getName(), createdRepoName,
                "Repository name mismatch");

        Assert.assertFalse(response.isPrivate(),
                "Repository visibility mismatch");

        Assert.assertEquals(response.getOwner().getLogin(), owner,
                "Repository owner mismatch");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {

        if (createdRepoName != null && owner != null) {

            Response deleteResponse =
                    gitHubClient.deleteRepo(owner, createdRepoName);

            Assert.assertTrue(
                    deleteResponse.statusCode() == 204
                            || deleteResponse.statusCode() == 404,
                    "Unexpected delete status: " + deleteResponse.statusCode()
            );
        }
    }
}