package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class NewBuildOrderPage {
    private final WebDriver driver;

    @FindBy(xpath = "//button[contains(text(),'В начало очереди')]")
    private WebElement headButton;

    @FindBy(xpath = "//button[contains(text(),'В конец очереди')]")
    private WebElement endButton;

    @FindBy(xpath = "//button[contains(text(),'Создать')]")
    private WebElement createButton;

    @FindBy(id = "buildingSelector")
    private WebElement buildingSelect;

    @FindBy(tagName = "textarea")
    private WebElement comment;

    public void editType(String type) {
        buildingSelect.click();
        driver.findElement(By.xpath("//option[contains(text(),'" + type + "')]")).click();
    }

    public NewBuildOrderPage(WebDriver driver) {
        this.driver = driver;
    }
}
