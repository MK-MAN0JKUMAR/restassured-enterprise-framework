package tests.base;

import framework.core.mock.WireMockManager;
import framework.data.DataSeedManager;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.*;

@Listeners({AllureTestNg.class})
public abstract class BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void startMockServer() {
        WireMockManager.start();
    }

    @AfterMethod(alwaysRun = true)
    public void stopMockServer() {
        WireMockManager.stop();
    }
}