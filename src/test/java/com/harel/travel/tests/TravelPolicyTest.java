package com.harel.travel.tests;

import com.harel.travel.core.BaseTest;
import com.harel.travel.pages.DateSelectionPage;
import com.harel.travel.pages.PassengersPage;
import com.harel.travel.pages.StartPage;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class TravelPolicyTest extends BaseTest {

    @Test
    public void travelPolicyFlow() {
        LocalDate departure = LocalDate.now().plusDays(7);
        LocalDate retorno = departure.plusDays(30);

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
