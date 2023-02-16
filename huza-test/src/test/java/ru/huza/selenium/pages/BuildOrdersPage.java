package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BuildOrdersPage {
    private final WebDriver driver;

    @FindBy(xpath = "//button[contains(text(),'Добавить заказ на строительство')]")
    private WebElement addBuildOrderButton;


    public List<BuildOrderRow> getBuildOrderRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(BuildOrderRow::new).collect(Collectors.toList());
    }

    public BuildOrdersPage(WebDriver driver) {
        this.driver = driver;
    }
}
