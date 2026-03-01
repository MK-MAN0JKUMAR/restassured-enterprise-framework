package tests.base;

import framework.core.metrics.MetricsCollector;
import framework.core.observability.CorrelationManager;
import framework.data.DataSeedManager;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({AllureTestNg.class})
public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void logSeed() {
        log.info("Data Seed: {}", DataSeedManager.getSeed());
    }

    @AfterMethod(alwaysRun = true)
    public void resetCorrelationId() {
        CorrelationManager.reset();
    }


    @AfterSuite(alwaysRun = true)
    public void printMetrics() {

        MetricsCollector.getAll().forEach((service, times) -> {

            if (times.isEmpty()) return;

            long max = times.stream().mapToLong(Long::longValue).max().orElse(0);
            double avg = times.stream().mapToLong(Long::longValue).average().orElse(0);

            System.out.println(
                    "Service: " + service +
                            " | Calls: " + times.size() +
                            " | Avg: " + avg +
                            " | Max: " + max
            );
        });
    }
}