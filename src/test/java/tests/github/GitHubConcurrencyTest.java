package tests.github;

import framework.client.GitHubClient;
import framework.data.github.GitHubDataFactory;
import framework.domain.github.CreateRepoRequest;
import framework.domain.github.RepoResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GitHubConcurrencyTest extends BaseTest {

    private final GitHubClient client = new GitHubClient();
    private final String owner = System.getenv("GITHUB_USERNAME");

    @Test(groups = {"github", "concurrency"})
    public void shouldHandleParallelRepoCreation() throws Exception {

        Assert.assertNotNull(owner, "GITHUB_USERNAME must be set");

        int threadCount = 5;   // keep safe to avoid rate limit
        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {

            results.add(executor.submit(() -> {

                CreateRepoRequest request =
                        GitHubDataFactory.createPublicRepo();

                String repoName = request.getName();

                Response create = client.createRepo(request);

                if (create.statusCode() != 201) {
                    return false;
                }

                RepoResponse mapped =
                        create.as(RepoResponse.class);

                if (!repoName.equals(mapped.getName())) {
                    return false;
                }

                Response delete =
                        client.deleteRepo(owner, repoName);

                return delete.statusCode() == 204
                        || delete.statusCode() == 404;
            }));
        }

        for (Future<Boolean> result : results) {
            Assert.assertTrue(result.get(),
                    "One of the concurrent operations failed");
        }

        executor.shutdown();
    }
}