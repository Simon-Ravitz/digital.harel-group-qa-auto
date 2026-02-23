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
        if (tryPickDateByHrlBo(start, end)) return this;
        if (tryPickDateByDataAttr(start, end)) return this;
        if (tryPickDateByAriaLabel(start, end)) return this;
        if (tryTypeIntoDateInputs(start, end)) return this;
        throw new NoSuchElementException("Could not select dates using available strategies");
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
        return this;
    }

    public PassengersPage goToPassengers() {
        clickByVisibleText("הלאה לפרטי הנוסעים");
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
                return;
            }
        }
    }

    private boolean tryPickDateByDataAttr(LocalDate start, LocalDate end) {
        String startIso = ISO_DATE.format(start);
        String endIso = ISO_DATE.format(end);

        By startBy = By.cssSelector("[data-date='" + startIso + "']");
        By endBy = By.cssSelector("[data-date='" + endIso + "']");

        if (!driver.findElements(startBy).isEmpty() && !driver.findElements(endBy).isEmpty()) {
            clickIfPresent(startBy);
            clickIfPresent(endBy);
            return true;
        }
        return false;
    }

    private boolean tryPickDateByHrlBo(LocalDate start, LocalDate end) {
        String startIso = ISO_DATE.format(start);
        String endIso = ISO_DATE.format(end);

        By startBy = By.cssSelector("[data-hrl-bo='" + startIso + "']:not([disabled])");
        By endBy = By.cssSelector("[data-hrl-bo='" + endIso + "']:not([disabled])");

        if (!driver.findElements(startBy).isEmpty() && !driver.findElements(endBy).isEmpty()) {
            clickIfPresent(startBy);
            clickIfPresent(endBy);
            return true;
        }
        return false;
    }

    private boolean tryPickDateByAriaLabel(LocalDate start, LocalDate end) {
        String startLabel = start.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        String endLabel = end.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));

        By startBy = By.cssSelector("[aria-label*='" + startLabel + "']");
        By endBy = By.cssSelector("[aria-label*='" + endLabel + "']");

        if (!driver.findElements(startBy).isEmpty() && !driver.findElements(endBy).isEmpty()) {
            clickIfPresent(startBy);
            clickIfPresent(endBy);
            return true;
        }
        return false;
    }

    private boolean tryTypeIntoDateInputs(LocalDate start, LocalDate end) {
        List<WebElement> inputs = driver.findElements(By.cssSelector("input[type='text'], input[type='date']"));
        if (inputs.size() < 2) return false;

        String startVal = DISPLAY_DDMMYYYY.format(start);
        String endVal = DISPLAY_DDMMYYYY.format(end);

        WebElement first = inputs.get(0);
        WebElement second = inputs.get(1);

        scrollIntoView(first);
        first.click();
        first.sendKeys(Keys.chord(Keys.CONTROL, "a"), startVal, Keys.TAB);

        scrollIntoView(second);
        second.click();
        second.sendKeys(Keys.chord(Keys.CONTROL, "a"), endVal, Keys.TAB);

        return true;
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
