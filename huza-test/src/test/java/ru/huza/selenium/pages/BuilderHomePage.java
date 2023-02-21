package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BuilderHomePage {
    private final WebDriver driver;

    public void waitUntilGetButtonLoaded() throws InterruptedException {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(getBuildOrderButton()));
    }

    private WebElement getBuildOrderButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Взять заказ на строительство')]"));
    }

    @FindBy(xpath = "//button[contains(text(),'Взять заказ на строительство')]")
    private WebElement getBuildOrderButton;

    public BuilderHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public List<BuildOrderRow> getBuildOrderRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(BuildOrderRow::new).collect(Collectors.toList());
    }
}
