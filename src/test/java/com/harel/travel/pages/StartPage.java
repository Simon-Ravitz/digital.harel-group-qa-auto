package com.harel.travel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class StartPage extends BasePage {
    private static final String DEFAULT_BASE_URL = "https://digital.harel-group.co.il/travel-policy";

    public StartPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public StartPage open() {
        String baseUrl = System.getProperty("baseUrl", "");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = DEFAULT_BASE_URL;
        }
        driver.get(baseUrl);
        return this;
    }

    public StartPage clickFirstPurchase() {
        clickByVisibleText("לרכישה בפעם הראשונה");
        return this;
    }

    public StartPage selectAnyContinent() {
        // Wait for destination tiles to render after first purchase click
        wait.until(driver -> !driver.findElements(By.cssSelector("[role='radio']")).isEmpty()
                || !driver.findElements(By.cssSelector("[id^='destination-']")).isEmpty());

        // Prefer the USA tile (data-hrl-bo) which is a role=radio container
        List<WebElement> usaTiles = driver.findElements(By.cssSelector("[data-hrl-bo='USA-selected'][role='radio']"));
        if (!usaTiles.isEmpty()) {
            WebElement tile = usaTiles.get(0);
            scrollIntoView(tile);
            tile.click();
            return this;
        }

        // Fallback: click any destination tile (role=radio)
        List<WebElement> radios = driver.findElements(By.cssSelector("[role='radio']"));
        if (!radios.isEmpty()) {
            WebElement tile = radios.get(0);
            scrollIntoView(tile);
            tile.click();
            return this;
        }

        // Fallback: click destination tiles by id prefix
        List<WebElement> dests = driver.findElements(By.cssSelector("[id^='destination-']"));
        if (!dests.isEmpty()) {
            WebElement tile = dests.get(0);
            scrollIntoView(tile);
            tile.click();
            return this;
        }

        throw new org.openqa.selenium.NoSuchElementException("Could not find a continent selection element");
    }

    public DateSelectionPage goToDateSelection() {
        clickByVisibleText("הלאה לבחירת תאריכי הנסיעה");
        return new DateSelectionPage(driver, wait);
    }
}
