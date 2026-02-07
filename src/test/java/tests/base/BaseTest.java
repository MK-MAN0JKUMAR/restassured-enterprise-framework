package tests.base;

import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        // Forces framework bootstrap early
        // Ensures config + logging visible at start
    }
}
