package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class NewAssetDefPage {
    private final WebDriver driver;

    @FindBy(id = "asset_def_name")
    private WebElement nameInput;

    @FindBy(id = "asset_def_description")
    private WebElement descriptionInput;

    @FindBy(id = "asset_def_type")
    private WebElement typeSelect;

    public void editType() {
        typeSelect.click();
        driver.findElement(By.xpath("//option[contains(text(),'Здание')]")).click();
    }

    @FindBy(xpath = "//div[contains(text(),'Добавить изображение')]")
    private WebElement imgInput;

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

    @FindBy(xpath = "//button[contains(text(),'Создать')]")
    private WebElement createButton;

    @FindBy(xpath = "//button[contains(text(),'Отмена')]")
    private WebElement cancelButton;

    public NewAssetDefPage(WebDriver driver) {
        this.driver = driver;
    }
}
