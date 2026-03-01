package tests.github;

import framework.client.GitHubClient;
import framework.data.github.GitHubDataFactory;
import framework.domain.github.CreateRepoRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class GitHubDuplicateRepoNegativeTest extends BaseTest {

    private final GitHubClient gitHubClient = new GitHubClient();
    private String repoName;

    private final String owner = System.getenv("GITHUB_USERNAME");

    @Test(groups = {"github", "negative", "github-negative"})
    public void shouldReturn422WhenRepositoryAlreadyExists() {

        Assert.assertNotNull(owner,
                "GITHUB_USERNAME environment variable must be set");

        // Step 1: Create repo
        CreateRepoRequest request =
                GitHubDataFactory.createPublicRepo();

        repoName = request.getName();

        Response firstCreate =
                gitHubClient.createRepo(request);

        Assert.assertEquals(firstCreate.statusCode(), 201,
                "Initial repo creation failed");

        // Step 2: Attempt duplicate creation
        Response duplicateCreate =
                gitHubClient.createRepo(request);

        Assert.assertEquals(duplicateCreate.statusCode(), 422,
                "Expected 422 for duplicate repo but got: "
                        + duplicateCreate.asString());

        String body = duplicateCreate.asString();

        Assert.assertTrue(body.contains("name already exists")
                        || body.contains("already exists"),
                "Expected duplicate name error message");

        Assert.assertTrue(body.contains("documentation_url"),
                "Missing documentation_url in error response");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {

        if (repoName != null && owner != null) {

            Response deleteResponse =
                    gitHubClient.deleteRepo(owner, repoName);

            Assert.assertTrue(
                    deleteResponse.statusCode() == 204
                            || deleteResponse.statusCode() == 404,
                    "Unexpected delete status: "
                            + deleteResponse.statusCode()
            );
        }
    }
}