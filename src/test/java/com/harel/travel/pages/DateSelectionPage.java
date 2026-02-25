package com.harel.travel.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateSelectionPage extends BasePage {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DateSelectionPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public DateSelectionPage pickDateRange(LocalDate start, LocalDate end) {
        openDatePicker();
        if (!tryPickDateByHrlBo(start, end)) {
            throw new NoSuchElementException("Could not select dates using data-hrl-bo");
        }
        return this;
    }

    public DateSelectionPage assertTotalDays(LocalDate start, LocalDate end) {
        long expectedExclusive = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        long expectedInclusive = expectedExclusive + 1;

        List<WebElement> candidates = driver.findElements(By.cssSelector("[data-hrl-bo='total-days']"));
        if (candidates.isEmpty()) {
            candidates = driver.findElements(By.xpath("//*[contains(text(),'סה\"כ') and contains(text(),'ימים')]"));
        }
        Assert.assertFalse(candidates.isEmpty(), "Total days summary not found");

        String text = candidates.get(0).getText();
        int days = extractFirstInt(text);
        Assert.assertTrue(days == expectedExclusive || days == expectedInclusive,
                "Expected total days to be " + expectedExclusive + " or " + expectedInclusive + " but was " + days + " (text: " + text + ")");
        com.harel.travel.core.ExtentLogger.info("Total days displayed: " + days);
        return this;
    }

    public PassengersPage goToPassengers() {
        clickByVisibleText("הלאה לפרטי הנוסעים");
        com.harel.travel.core.ExtentLogger.info("Click: הלאה לפרטי הנוסעים");
        return new PassengersPage(driver, wait);
    }

    private void openDatePicker() {
        List<By> openers = List.of(
                By.cssSelector("input[name*='departure']"),
                By.cssSelector("input[name*='start']"),
                By.cssSelector("input[placeholder*='יציאה']"),
                By.cssSelector("[data-testid='departure-date']"),
                By.cssSelector("[data-test='departure-date']"),
                By.xpath("//*[contains(@class,'calendar') or contains(@class,'date')]//button")
        );
        for (By by : openers) {
            List<WebElement> els = driver.findElements(by);
            if (!els.isEmpty()) {
                WebElement el = els.get(0);
                scrollIntoView(el);
                el.click();
                com.harel.travel.core.ExtentLogger.info("Open date picker");
                return;
            }
        }
    }

    private boolean pickDateByHrlBo(LocalDate start, LocalDate end) {
        String startIso = ISO_DATE.format(start);
        String endIso = ISO_DATE.format(end);

        By startBy = By.cssSelector("[data-hrl-bo='" + startIso + "']:not([disabled])");
        By endBy = By.cssSelector("[data-hrl-bo='" + endIso + "']:not([disabled])");

        if (!driver.findElements(startBy).isEmpty() && !driver.findElements(endBy).isEmpty()) {
            clickIfPresent(startBy);
            clickIfPresent(endBy);
            com.harel.travel.core.ExtentLogger.info("Pick dates by data-hrl-bo: " + startIso + " -> " + endIso);
            return true;
        }
        return false;
    }

    private void clickIfPresent(By by) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
        scrollIntoView(el);
        el.click();
    }

    private int extractFirstInt(String text) {
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return -1;
        return Integer.parseInt(digits);
    }
}
