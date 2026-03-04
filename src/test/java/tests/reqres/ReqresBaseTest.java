package tests.reqres;

import framework.core.mock.WireMockManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import tests.base.BaseTest;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ReqresBaseTest extends BaseTest {

    private boolean wiremockStarted = false;

    @BeforeClass(alwaysRun = true)
    public void startMock() {

        String serviceParam = System.getProperty("service");

        if (serviceParam == null || serviceParam.equalsIgnoreCase("all")) {

            WireMockManager.start();
            wiremockStarted = true;
            return;
        }

        Set<String> services =
                Arrays.stream(serviceParam.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());

        if (services.contains("reqres")) {

            WireMockManager.start();
            wiremockStarted = true;
        }
    }

    @AfterClass(alwaysRun = true)
    public void stopMock() {

        if (wiremockStarted) {
            WireMockManager.stop();
        }
    }
}