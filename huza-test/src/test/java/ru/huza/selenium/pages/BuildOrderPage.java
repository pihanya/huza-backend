package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class BuildOrderPage {
    private final WebDriver driver;
    @FindBy(xpath = "//button[contains(text(),'Взять в работу')]")
    private WebElement acceptBuildOrderButton;

    @FindBy(xpath = "//button[contains(text(),'Отклонить')]")
    private WebElement denyBuildOrderButton;

    public BuildOrderPage(WebDriver driver) {
        this.driver = driver;
    }
}
