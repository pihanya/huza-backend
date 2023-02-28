package ru.huza.selenium.ui;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.huza.selenium.pages.AdminHomePage;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminHomePageTests extends SeleniumTestBase {

    private AdminHomePage homePage;

    private String homePageUrl;

    private boolean authenticated = false;

    @BeforeEach
    public void setUp() throws InterruptedException {
        if (!authenticated) {
            this.authenticate("admin@itmo.ru", "password");
            authenticated = true;
        }
        this.get("/home");
        homePage = PageFactory.initElements(driver, AdminHomePage.class);
        homePageUrl = driver.getCurrentUrl();
    }


    private void testRedirect(WebElement card, String destination) {
        card.click();
        this.redirectWait(homePageUrl);
        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }

    @Test
    public void RedirectToUserManagement() {
        testRedirect(homePage.getUserManagementCard(), "/home/users");
    }

    @Test
    public void RedirectToAssetDefManagement() {
        testRedirect(homePage.getAssetDefCard(), "/home/asset_definitions");
    }

    @Test
    public void RedirectToAudit() {
        testRedirect(homePage.getAuditCard(), "/home/audit");
    }

    @Test
    public void RedirectToResources() {
        testRedirect(homePage.getResourcesCard(), "/home/resources");
    }

    @Test
    public void RedirectToRecruits() {
        testRedirect(homePage.getUnitsCard(), "/home/recruits");
    }

    @Test
    public void RedirectToSpells() {
        testRedirect(homePage.getSpellsCard(), "/home/spells");
    }

    @Test
    public void RedirectToBuildings() {
        testRedirect(homePage.getBuldingsCard(), "/home/buildings");
    }

}
