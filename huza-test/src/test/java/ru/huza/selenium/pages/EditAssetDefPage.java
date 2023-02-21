package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class EditAssetDefPage {
    private final WebDriver driver;

    @FindBy(id = "asset_def_name")
    private WebElement nameInput;

    @FindBy(id = "asset_def_description")
    private WebElement descriptionInput;

    @FindBy(id = "asset_def_type")
    private WebElement typeSelect;

    public void editType(String category) {
        typeSelect.click();
        driver.findElement(By.xpath("//option[contains(text(),'" + category + "')]")).click();
    }

    public void editMagicSchool(String magicSchool) {
        typeSelect.click();
        driver.findElement(By.xpath("//option[contains(text(),'" + magicSchool + "')]")).click();
    }

    @FindBy(id = "cost_STONE")
    private WebElement stoneCostInput;

    @FindBy(id = "cost_WOOD")
    private WebElement woodCostInput;

    @FindBy(id = "cost_GOLD")
    private WebElement goldCostInput;

    @FindBy(id = "cost_GEM")
    private WebElement gemCostInput;

    @FindBy(id = "cost_CRYSTAL")
    private WebElement crystalCostInput;

    @FindBy(id = "cost_MERCURY")
    private WebElement mercuryCostInput;

    @FindBy(xpath = "//button[contains(text(),'Сохранить')]")
    private WebElement saveButton;

    @FindBy(xpath = "//button[contains(text(),'Отмена')]")
    private WebElement cancelButton;

    public WebElement getSaveConfirmButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Сохранить!')]"));
    }

    public WebElement getFractionInput() {
        return this.driver.findElement(By.id("asset_def_recruit_fraction"));
    }

    public WebElement getRecruitLevelInput() {
        return this.driver.findElement(By.id("asset_def_recruit_level"));
    }

    public EditAssetDefPage(WebDriver driver) {
        this.driver = driver;
    }
}
