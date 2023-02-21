package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.huza.selenium.pages.NewUnitPage;
import ru.huza.selenium.pages.UnitRow;
import ru.huza.selenium.pages.WarriorHomePage;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnitManagementTests extends SeleniumTestBase {
    @Test
    public void test() throws InterruptedException {
        this.driver.get(this.composeUrl("/"));

        final String wizardEmail = "warrior@itmo.ru", wizardPass = "password";
        this.authenticate(wizardEmail, wizardPass);

        this.get("/home");

        final var warriorHomePage = this.initPage(WarriorHomePage.class);
        String warriorHomePageUrl = driver.getCurrentUrl();

        String unitName = "Архангел";
        int expectedRowsCount = warriorHomePage.getUnitRows().size() + 1;
        warriorHomePage.getAddUnitButton().click();
        this.redirectWait(warriorHomePageUrl);

        final var newUnitPage = this.initPage(NewUnitPage.class);
        String newUnitPageUrl = driver.getCurrentUrl();

        List<UnitRow> unitRows = newUnitPage.getUnitRows();
        UnitRow unitRow = unitRows.stream()
                .filter(unitRow1 -> unitRow1.getName().equals(unitName))
                .findAny()
                .orElse(null);

        unitRow.selectRow();
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        WebElement ele = driver.findElement(By.xpath("//button[contains(text(),'Добавить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        newUnitPage.waitUntilAddUnitButtonLoaded(unitRows.size());
        newUnitPage.getAddUnitButton().click();
        this.redirectWait(newUnitPageUrl);

        Assertions.assertEquals(warriorHomePage.getUnitRows().size(), expectedRowsCount);


        unitRows = warriorHomePage.getUnitRows();
        unitRow = unitRows.stream()
                .filter(unitRow1 -> unitRow1.getName().equals(unitName))
                .findAny()
                .orElse(null);
        unitRow.selectRow();
        String updatedValue = "25";
        ele = driver.findElement(By.xpath("//button[contains(text(),'Удалить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        warriorHomePage.waitUntilButtonLoaded(unitRows.size(), warriorHomePage.getEditButton());
        warriorHomePage.getEditButton().click();
        warriorHomePage.getEditInput().clear();
        warriorHomePage.getEditInput().sendKeys(updatedValue);
        warriorHomePage.getSaveButton().click();
        UnitRow updatedUnitRow = warriorHomePage.getUnitRows().stream()
                .filter(unitRow1 -> unitRow1.getName().equals(unitName) && unitRow1.getCount().equals(updatedValue))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(updatedUnitRow);

        expectedRowsCount = expectedRowsCount - 1;

        unitRows = warriorHomePage.getUnitRows();
        unitRow = unitRows.stream()
                .filter(unitRow1 -> unitRow1.getName().equals(unitName))
                .findAny()
                .orElse(null);
        unitRow.selectRow();
        ele = driver.findElement(By.xpath("//button[contains(text(),'Удалить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        warriorHomePage.waitUntilButtonLoaded(unitRows.size(), warriorHomePage.getDeleteButton());
        warriorHomePage.getDeleteButton().click();

        //Assertions.assertEquals(warriorHomePage.getUnitRows().size(), expectedRowsCount); //todo: uncomment when it will work =)
    }
}
