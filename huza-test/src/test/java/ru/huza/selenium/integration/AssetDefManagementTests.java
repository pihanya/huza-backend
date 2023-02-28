package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.context.jdbc.Sql;
import ru.huza.selenium.pages.*;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssetDefManagementTests extends SeleniumTestBase {

    @Test
    public void test() throws AWTException, URISyntaxException, InterruptedException {
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

//        String imgFile = "images/testpic.jpg"; //todo: move to constants
//        WebElement addFile = newAssetDefPage.getImgInput();
//        addFile.click();
//        this.uploadImg(imgFile);

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

        newAssetDefRow.selectRow();
        this.redirectWait(assetDefsPageUrl);

        String editAssetDefPageUrl = driver.getCurrentUrl();
        final var editAssetDefPage = this.initPage(EditAssetDefPage.class);
        String editedName= "Измененное название", editedDescription = "Изменненое тестовое описание";
        String editedGoldCost = "20", editedCost = "Древесина - 50, Золото - 20";;
        editAssetDefPage.getNameInput().clear();
        editAssetDefPage.getNameInput().sendKeys(editedName);
        editAssetDefPage.getDescriptionInput().clear();
        editAssetDefPage.getDescriptionInput().sendKeys(editedDescription);
        editAssetDefPage.getGoldCostInput().clear();
        editAssetDefPage.getGoldCostInput().sendKeys(editedGoldCost);
        editAssetDefPage.getSaveButton().submit();
        editAssetDefPage.getSaveConfirmButton().click();
        this.redirectWait(editAssetDefPageUrl);

        assetDefRowList = assetDefsPage.getAssetDefRows();
        AssetDefRow editedAssetDefRow = assetDefRowList.stream()
                .filter(assetDefRow -> assetDefRow.getName().equals(editedName))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(editedAssetDefRow);
        Assertions.assertEquals(editedAssetDefRow.getName(), editedName);
        Assertions.assertEquals(editedAssetDefRow.getCost(), editedCost);
    }
}
