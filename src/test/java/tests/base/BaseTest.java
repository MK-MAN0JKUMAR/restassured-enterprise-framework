package tests.base;

import framework.core.mock.WireMockManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import static com.github.tomakehurst.wiremock.client.WireMock.reset;


public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void startMockServer() {
        WireMockManager.start();
        System.setProperty("base.url", "http://localhost:" + WireMockManager.port());
    }

    @AfterSuite(alwaysRun = true)
    public void stopMockServer() {
        WireMockManager.stop();
    }

    @BeforeMethod(alwaysRun = true)
    public void resetWireMock() {
        WireMockManager.getServer().resetAll();
    }
}
