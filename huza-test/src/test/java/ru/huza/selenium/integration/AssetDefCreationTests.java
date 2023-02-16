package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.jdbc.Sql;
import ru.huza.selenium.pages.AdminHomePage;
import ru.huza.selenium.pages.AssetDefRow;
import ru.huza.selenium.pages.AssetDefsPage;
import ru.huza.selenium.pages.NewAssetDefPage;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

@SeleniumTest
//@Sql(value = {"/initScripts/create-admin.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/initScripts/delete-admin.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssetDefCreationTests extends SeleniumTestBase {

    @Test
    public void test() throws AWTException {
        this.driver.get(this.composeUrl("/"));

        final String adminEmail = "admin@itmo.ru", adminPass = "password";
        this.authenticate(adminEmail, adminPass);

        this.get("/home");
        final var adminHomePage = this.initPage(AdminHomePage.class);
        String homePageUrl = driver.getCurrentUrl();
        adminHomePage.getAssetDefCard().click();
        this.redirectWait(homePageUrl);

        final var assetDefsPage = this.initPage(AssetDefsPage.class);
        String assetDefsPageUrl = driver.getCurrentUrl();
        assetDefsPage.getAddAssetDefButton().click();
        this.redirectWait(assetDefsPageUrl);

        String newAssetDefPageUrl = driver.getCurrentUrl();
        final var newAssetDefPage = this.initPage(NewAssetDefPage.class);

        final String newAssetDefName = "Тестовое здание", newAssetDefDescription = "Описание тестового здания";
        final String newAssetDefCategory = "Здание";
        final String woodCost = "50", goldCost = "100", expectedCost = "Древесина - 50, Золото - 100";

        newAssetDefPage.getNameInput().sendKeys(newAssetDefName);
        newAssetDefPage.getDescriptionInput().sendKeys(newAssetDefDescription);

        String imgFile = "/home/nikolay/Загрузки/8adefe5af862b4f9cec286c6ee4722cb.jpg"; //todo: move to constants
        WebElement addFile = newAssetDefPage.getImgInput();
        addFile.click();
        this.uploadImg(imgFile);

        newAssetDefPage.editType(newAssetDefCategory);
        newAssetDefPage.getWoodCostInput().sendKeys(woodCost);
        newAssetDefPage.getGoldCostInput().sendKeys(goldCost);

        newAssetDefPage.getCreateButton().submit();
        this.redirectWait(newAssetDefPageUrl);

        List<AssetDefRow> assetDefRowList = assetDefsPage.getAssetDefRows();
        AssetDefRow newAssetDefRow = assetDefRowList.stream()
                .filter(assetDefRow -> assetDefRow.getName().equals(newAssetDefName))
                .findAny()
                .orElse(null);

        Assertions.assertNotNull(newAssetDefRow);
        Assertions.assertEquals(newAssetDefRow.getName(), newAssetDefName);
        Assertions.assertEquals(newAssetDefRow.getCategory(), newAssetDefCategory);
        Assertions.assertEquals(newAssetDefRow.getCost(), expectedCost);
    }
}
