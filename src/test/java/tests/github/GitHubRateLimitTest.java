package tests.github;

import framework.client.GitHubClient;
import framework.core.validation.RateLimitValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

public class GitHubRateLimitTest extends BaseTest {

    private final GitHubClient gitHubClient = new GitHubClient();

    @Test(groups = {"github", "resilience"})
    public void shouldExposeValidRateLimitHeaders() {

        Response response = gitHubClient.getRepo(
                System.getenv("GITHUB_USERNAME"),
                "non-existing-repo-for-rate-check"
        );

        // 404 expected but headers must exist
        Assert.assertTrue(
                response.statusCode() == 200
                        || response.statusCode() == 404,
                "Unexpected status: " + response.statusCode()
        );

        RateLimitValidator.validate(response);
    }
}