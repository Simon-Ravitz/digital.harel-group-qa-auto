package com.harel.travel.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;
import java.util.logging.Logger;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger logger = Logger.getLogger(getClass().getName());

    @BeforeClass
    public void setup() {
        driver = DriverFactory.createChrome();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        logger.info("Driver: " + driver.getClass().getName());
        if (driver instanceof org.openqa.selenium.remote.RemoteWebDriver) {
            logger.info("Capabilities: " + ((org.openqa.selenium.remote.RemoteWebDriver) driver).getCapabilities());
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
