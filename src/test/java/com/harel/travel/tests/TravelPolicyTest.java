package com.harel.travel.tests;

import com.harel.travel.core.BaseTest;
import com.harel.travel.pages.DateSelectionPage;
import com.harel.travel.pages.PassengersPage;
import com.harel.travel.pages.StartPage;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.ZoneId;

public class TravelPolicyTest extends BaseTest {

    @Test
    public void travelPolicyFlow() {
        ZoneId tz = ZoneId.of(System.getProperty("tz", "Asia/Jerusalem"));
        LocalDate departure = LocalDate.now(tz).plusDays(7);
        LocalDate retorno = departure.plusDays(30);

        com.harel.travel.core.ExtentLogger.info("Start test with departure=" + departure + ", return=" + retorno);

        DateSelectionPage dates = new StartPage(driver, wait)
                .open()
                .clickFirstPurchase()
                .selectAnyContinent()
                .goToDateSelection();

        PassengersPage passengers = dates
                .pickDateRange(departure, retorno)
                .assertTotalDays(departure, retorno)
                .goToPassengers();

        passengers.assertOpened();
    }
}
