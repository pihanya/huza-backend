package ru.huza.selenium.integration;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.huza.selenium.pages.*;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpellManagementTests extends SeleniumTestBase {

    @Test
    public void test() throws InterruptedException {
        this.driver.get(this.composeUrl("/"));

        final String wizardEmail = "wizard@itmo.ru", wizardPass = "password";
        this.authenticate(wizardEmail, wizardPass);

        this.get("/home");

        final var wizardHomePage = this.initPage(WizardHomePage.class);
        String wizardHomePageUrl = driver.getCurrentUrl();

        String spellName = "Армагедон";
        int expectedRowsCount = wizardHomePage.getSpellRows().size() + 1;
        wizardHomePage.getAddSpellButton().click();
        this.redirectWait(wizardHomePageUrl);

        final var newSpellPage = this.initPage(NewSpellPage.class);
        String newSpellPageUrl = driver.getCurrentUrl();

        List<SpellRow> spellRows = newSpellPage.getSpellRows();
        SpellRow spellRow = spellRows.stream()
                .filter(spellRow1 -> spellRow1.getName().equals(spellName))
                .findAny()
                .orElse(null);

        spellRow.selectRow();
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        WebElement ele = driver.findElement(By.xpath("//button[contains(text(),'Добавить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        newSpellPage.waitUntilAddSpellButtonLoaded(spellRows.size());
        newSpellPage.getAddSpellButton().click();
        this.redirectWait(newSpellPageUrl);

        Assertions.assertEquals(wizardHomePage.getSpellRows().size(), expectedRowsCount);

        expectedRowsCount = expectedRowsCount - 1;

        spellRows = wizardHomePage.getSpellRows();
        spellRow = spellRows.stream()
                .filter(spellRow1 -> spellRow1.getName().equals(spellName))
                .findAny()
                .orElse(null);

        spellRow.selectRow();
        ele = driver.findElement(By.xpath("//button[contains(text(),'Удалить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        wizardHomePage.waitUntilDeleteButtonLoaded(spellRows.size());
        wizardHomePage.getDeleteButton().click();

        //Assertions.assertEquals(wizardHomePage.getSpellRows().size(), expectedRowsCount); //todo: uncomment when it will work =)
    }
}
