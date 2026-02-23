package com.harel.travel.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected void clickByVisibleText(String text) {
        By label = By.xpath("//span[contains(@class,'MuiButton-label') and normalize-space(.)='" + text + "']");
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(label));
        el.click();
    }

    protected void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
        } catch (Exception ignored) {
            new Actions(driver).moveToElement(el).perform();
        }
    }
}
