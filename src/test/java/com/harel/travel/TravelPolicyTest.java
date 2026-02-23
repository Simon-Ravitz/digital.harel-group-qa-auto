package com.harel.travel;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TravelPolicyTest {
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String DEFAULT_BASE_URL = "https://digital.harel-group.co.il/travel-policy";
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void travelPolicyFlow() {
        String baseUrl = System.getProperty("baseUrl", DEFAULT_BASE_URL);
        driver.get(baseUrl);

        clickByVisibleText("לרכישה בפעם הראשונה");

        selectAnyContinent();

        clickByVisibleText("הלאה לבחירת תאריכי הנסיעה");

        LocalDate departure = LocalDate.now().plusDays(7);
        LocalDate retorno = departure.plusDays(30);

        pickDateRange(departure, retorno);

        assertTotalDays(departure, retorno);

        clickByVisibleText("הלאה לפרטי הנוסעים");

        assertPassengersPageOpened();
    }

    private void clickByVisibleText(String text) {
        By button = By.xpath("//button[normalize-space(.)='" + text + "']");
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    private void selectAnyContinent() {
        // Try common selectable card/buttons for continents
        List<By> continentCandidates = List.of(
                By.cssSelector("[data-test='continent-card']"),
                By.cssSelector("[data-testid='continent-card']"),
                By.cssSelector("[role='button'][data-continent]"),
                By.xpath("//*[contains(@class,'continent')]//button"),
                By.xpath("//*[contains(@class,'continent')]//*[self::div or self::button][@role='button']")
        );

        for (By by : continentCandidates) {
            List<WebElement> elements = driver.findElements(by);
            if (!elements.isEmpty()) {
                WebElement first = elements.get(0);
                scrollIntoView(first);
                first.click();
                return;
            }
        }

        // Fallback: click first large selectable tile on page
        List<WebElement> buttons = driver.findElements(By.xpath("//button"));
        for (WebElement b : buttons) {
            String label = b.getText().trim();
            if (!label.isEmpty() && !label.contains("הלאה")) {
                scrollIntoView(b);
                b.click();
                return;
            }
        }

        throw new NoSuchElementException("Could not find a continent selection element");
    }

    private void pickDateRange(LocalDate start, LocalDate end) {
        // Try to open date picker by clicking departure field or calendar icon
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
                break;
            }
        }

        if (tryPickDateByDataAttr(start, end)) return;
        if (tryPickDateByAriaLabel(start, end)) return;
        if (tryTypeIntoDateInputs(start, end)) return;

        throw new NoSuchElementException("Could not select dates using available strategies");
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

    private void assertTotalDays(LocalDate start, LocalDate end) {
        long expected = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        String expectedStr = String.valueOf(expected);

        // Look for a summary element containing total days
        List<WebElement> candidates = driver.findElements(By.xpath("//*[contains(text(),'סה\"כ') and contains(text(),'ימים')]"));
        if (candidates.isEmpty()) {
            candidates = driver.findElements(By.xpath("//*[contains(text(),'סה') and contains(text(),'ימים')]"));
        }
        Assert.assertFalse(candidates.isEmpty(), "Total days summary not found");

        boolean matched = candidates.stream().anyMatch(el -> el.getText().contains(expectedStr));
        Assert.assertTrue(matched, "Expected total days to include: " + expectedStr);
    }

    private void assertPassengersPageOpened() {
        // Check for a known heading or form section
        List<WebElement> headings = driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::h3][contains(text(),'נוסעים') or contains(text(),'פרטי הנוסעים')]"));
        Assert.assertFalse(headings.isEmpty(), "Passengers page did not open (no heading found)");
    }

    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
        } catch (Exception ignored) {
            new Actions(driver).moveToElement(el).perform();
        }
    }
}
