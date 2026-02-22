package tests.base;

import framework.data.DataSeedManager;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({AllureTestNg.class})
public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void logSeed() {
        log.info("Data Seed: {}", DataSeedManager.getSeed());
    }
}