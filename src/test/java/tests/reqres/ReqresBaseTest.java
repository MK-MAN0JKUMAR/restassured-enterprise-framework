package tests.reqres;

import framework.core.mock.WireMockManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import tests.base.BaseTest;

public abstract class ReqresBaseTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void startMock() {
        WireMockManager.start();
    }

    @AfterClass(alwaysRun = true)
    public void stopMock() {
        WireMockManager.stop();
    }
}