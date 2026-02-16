package tests.base;

import framework.core.mock.WireMockManager;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import static com.github.tomakehurst.wiremock.client.WireMock.reset;

@Listeners({AllureTestNg.class})
public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void startMockServer() {
        WireMockManager.start();
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
