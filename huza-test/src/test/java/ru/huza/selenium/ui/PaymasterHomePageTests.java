package ru.huza.selenium.ui;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.huza.selenium.pages.BuilderHomePage;
import ru.huza.selenium.pages.PaymasterHomePage;
import ru.huza.selenium.pages.ResourceRow;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymasterHomePageTests extends SeleniumTestBase {
    private PaymasterHomePage homePage;

    private String homePageUrl;

    private boolean authenticated = false;

    @BeforeEach
    public void setUp() throws InterruptedException {
        if (!authenticated) {
            this.authenticate("paymaster@itmo.ru", "password");
            authenticated = true;
        }
        this.get("/home");
        homePage = PageFactory.initElements(driver, PaymasterHomePage.class);
        homePageUrl = driver.getCurrentUrl();
    }


    @Test
    public void showResources() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        Assertions.assertNotNull(resourceRows);
    }

    @Test
    public void showGold() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Золото"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showOre() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Руда"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showWood() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Древесина"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showQuicksilver() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Ртуть"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showSulfur() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Сера"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showCrystal() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Кристаллы"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }

    @Test
    public void showGems() {
        List<ResourceRow> resourceRows = homePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals("Самоцветы"))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(resourceRow);
    }
}
