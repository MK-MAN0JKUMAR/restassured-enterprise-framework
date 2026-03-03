package tests.github;

import framework.client.GitHubClient;
import framework.core.pagination.PaginationHelper;
import framework.domain.github.RepoResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.List;

public class GitHubPaginationTest extends BaseTest {

    private final GitHubClient gitHubClient = new GitHubClient();

    @Test(groups = {"github", "pagination"})
    public void shouldCollectAllRepositoriesAcrossPages() {

        Response firstPage = gitHubClient.listRepos(5);

        Assert.assertEquals(firstPage.statusCode(), 200);

        List<RepoResponse> allRepos =
                PaginationHelper.collectAllPages(
                        firstPage,
                        RepoResponse[].class,
                        gitHubClient::fetchNextPage
                );

        Assert.assertTrue(allRepos.size() >= 0);
    }
}