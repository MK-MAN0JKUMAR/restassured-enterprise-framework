package tests.reqres;

import framework.core.mock.WireMockManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import tests.base.BaseTest;

public abstract class ReqresBaseTest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startMock() {
        WireMockManager.start();
    }

    @AfterMethod(alwaysRun = true)
    public void stopMock() {
        WireMockManager.stop();
    }
}