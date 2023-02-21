package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.huza.selenium.pages.*;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuildOrderTests extends SeleniumTestBase {
    @Test
    public void test() throws InterruptedException {
        this.driver.get(this.composeUrl("/"));

        final String ownerEmail = "owner@itmo.ru", ownerPass = "password";
        this.authenticate(ownerEmail, ownerPass);

        this.get("/home");
        final var ownerHomePage = this.initPage(OwnerHomePage.class);
        String ownerHomePageUrl = driver.getCurrentUrl();
        ownerHomePage.getBuildCard().click();
        this.redirectWait(ownerHomePageUrl);

        final var buildOrdersPage = this.initPage(BuildOrdersPage.class);
        final int expectedRowsCount = buildOrdersPage.getBuildOrderRows().size() + 3;

        String buildOrdersPageUrl = driver.getCurrentUrl();
        buildOrdersPage.getAddBuildOrderButton().click();
        this.redirectWait(buildOrdersPageUrl);

        boolean headOrder = true;
        String buildingType = "Ратуша", comment = "Очень срочно нужно!";
        this.createBuildOrder(headOrder, buildingType, comment);

        buildOrdersPage.getAddBuildOrderButton().click();
        this.redirectWait(buildOrdersPageUrl);
        buildingType = "Таверна";
        comment = "Еще срочнее нужно!";
        this.createBuildOrder(headOrder, buildingType, comment);

        buildOrdersPage.getAddBuildOrderButton().click();
        this.redirectWait(buildOrdersPageUrl);
        headOrder = false;
        buildingType = "Магистрат";
        comment = "Это не очень нужно!";
        this.createBuildOrder(headOrder, buildingType, comment);

        List<BuildOrderRow> buildOrderRows = buildOrdersPage.getBuildOrderRows();
        Assertions.assertEquals(buildOrderRows.size(), expectedRowsCount);

        this.logout();

        final String builderEmail = "builder@itmo.ru", builderPass = "password";
        this.authenticate(builderEmail, builderPass);

        this.get("/home");
        final var builderHomePage = this.initPage(BuilderHomePage.class);
        String builderHomePageUrl = driver.getCurrentUrl();
        builderHomePage.waitUntilGetButtonLoaded();

        builderHomePage.getGetBuildOrderButton().click();
        this.redirectWait(builderHomePageUrl);
        String buildOrderPageUrl = driver.getCurrentUrl();
        var buildOrderPage = this.initPage(BuildOrderPage.class);
        buildOrderPage.getAcceptBuildOrderButton().click();
        this.redirectWait(buildOrderPageUrl);

//        builderHomePage.getGetBuildOrderButton().click();
//        this.redirectWait(builderHomePageUrl);
//        buildOrderPageUrl = driver.getCurrentUrl();
//        buildOrderPage.getDenyBuildOrderButton().click();
//        this.redirectWait(buildOrderPageUrl);

        buildOrderRows = buildOrdersPage.getBuildOrderRows();
        BuildOrderRow buildOrderRow1 = buildOrderRows.stream()
                .filter(buildOrderRow -> buildOrderRow.getOrder().equals("1"))
                .findAny()
                .orElse(null);
        BuildOrderRow buildOrderRow2 = buildOrderRows.stream()
                .filter(buildOrderRow -> buildOrderRow.getOrder().equals("2"))
                .findAny()
                .orElse(null);

        Assertions.assertNotNull(buildOrderRow1);
        Assertions.assertNotNull(buildOrderRow2);
        Assertions.assertEquals(buildOrderRow1.getStatus(), "В процессе");
        Assertions.assertEquals(buildOrderRow2.getStatus(), "Создан");
    }
}
