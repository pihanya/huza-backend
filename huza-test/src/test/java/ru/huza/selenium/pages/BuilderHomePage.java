package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BuilderHomePage {
    private final WebDriver driver;

    public BuilderHomePage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[contains(text(),'Взять заказ на строительство')]")
    private WebElement getBuildOrderButton;


    public List<BuildOrderRow> getBuildOrderRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(BuildOrderRow::new).collect(Collectors.toList());
    }
}
