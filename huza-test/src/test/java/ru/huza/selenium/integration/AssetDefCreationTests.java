package ru.huza.selenium.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.jdbc.Sql;
import ru.huza.selenium.pages.AdminHomePage;
import ru.huza.selenium.pages.AssetDefsPage;
import ru.huza.selenium.pages.LoginPage;
import ru.huza.selenium.pages.NewAssetDefPage;
import ru.huza.selenium.unit.AdminHomePageTests;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

@SeleniumTest
@Sql(value = {"/initScripts/create-admin.sql", "/initScripts/create-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssetDefCreationTests extends SeleniumTestBase {

    @Test
    public void test() {
        this.driver.get(this.composeUrl("/"));

        this.authenticate("testadmin@itmo.ru", "password");

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
        newAssetDefPage.getNameInput().sendKeys("Тестовое здание");
        newAssetDefPage.getDescriptionInput().sendKeys("Описание тестового здания");
        newAssetDefPage.getImgInput().sendKeys("/home/nikolay/Загрузки/8adefe5af862b4f9cec286c6ee4722cb.jpg");
        newAssetDefPage.editType();
        newAssetDefPage.getWoodCostInput().sendKeys("50");
        newAssetDefPage.getGoldCostInput().sendKeys("100");
        newAssetDefPage.getCreateButton().click();
        this.redirectWait(newAssetDefPageUrl);


    }
}
