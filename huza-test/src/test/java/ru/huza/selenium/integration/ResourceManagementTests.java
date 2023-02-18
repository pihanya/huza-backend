package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.huza.selenium.pages.PaymasterHomePage;
import ru.huza.selenium.pages.ResourceRow;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourceManagementTests extends SeleniumTestBase {
    @Test
    public void test() throws InterruptedException {
        this.driver.get(this.composeUrl("/"));

        final String paymasterEmail = "paymaster@itmo.ru", paymasterPass = "password";
        this.authenticate(paymasterEmail, paymasterPass);

        this.get("/home");

        final var paymasterHomePage = this.initPage(PaymasterHomePage.class);

        String resourceName = "Золото", expectedCount = "322";

        List<ResourceRow> resourceRows = paymasterHomePage.getResourceRows();
        ResourceRow resourceRow = resourceRows.stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals(resourceName))
                .findAny()
                .orElse(null);
        resourceRow.selectRow();

        JavascriptExecutor js = ((JavascriptExecutor) driver);
        WebElement ele = driver.findElement(By.xpath("//button[contains(text(),'Изменить')]"));
        js.executeScript("arguments[0].scrollIntoView(true);", ele);
        paymasterHomePage.waitUntilEditButtonLoaded(resourceRows.size());

        paymasterHomePage.getEditButton().click();
        paymasterHomePage.getNewValueInput().clear();
        paymasterHomePage.getNewValueInput().sendKeys(expectedCount);
        paymasterHomePage.getSaveButton().click();

        ResourceRow editedResourceRow = paymasterHomePage.getResourceRows().stream()
                .filter(resourceRow1 -> resourceRow1.getName().equals(resourceName))
                .findAny()
                .orElse(null);

        Assertions.assertEquals(editedResourceRow.getCount(), expectedCount);
    }
}
