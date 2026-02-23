package com.harel.travel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class PassengersPage extends BasePage {
    public PassengersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public PassengersPage assertOpened() {
        List<WebElement> headings = driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::h3][contains(text(),'נוסעים') or contains(text(),'פרטי הנוסעים')]"));
        Assert.assertFalse(headings.isEmpty(), "Passengers page did not open (no heading found)");
        Assert.assertTrue(driver.getCurrentUrl().contains("/travel-policy/wizard/travelers"),
                "Unexpected URL: " + driver.getCurrentUrl());
        return this;
    }
}
