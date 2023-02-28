package ru.huza.selenium.ui;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.huza.selenium.pages.BuilderHomePage;
import ru.huza.selenium.pages.UnitRow;
import ru.huza.selenium.pages.WarriorHomePage;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WarriorHomePageTests extends SeleniumTestBase {
    private WarriorHomePage homePage;

    private String homePageUrl;

    private boolean authenticated = false;

    @BeforeEach
    public void setUp() throws InterruptedException {
        if (!authenticated) {
            this.authenticate("warrior@itmo.ru", "password");
            authenticated = true;
        }
        this.get("/home");
        homePage = PageFactory.initElements(driver, WarriorHomePage.class);
        homePageUrl = driver.getCurrentUrl();
    }


    private void testRedirect(WebElement card, String destination) {
        card.click();
        this.redirectWait(homePageUrl);
        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }


    @Test
    public void RedirectToNewRecruit() {
        testRedirect(homePage.getAddUnitButton(), "/home/recruits/new");
    }

    @Test
    public void showRecruits() {
        List<UnitRow> unitRows = homePage.getUnitRows();
        Assertions.assertNotNull(unitRows);
    }
}
