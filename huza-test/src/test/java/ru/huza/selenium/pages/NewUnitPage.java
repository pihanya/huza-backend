package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class NewUnitPage {

    private final WebDriver driver;

    public void waitUntilAddUnitButtonLoaded(int rowsCount) throws InterruptedException {
        Thread.sleep(rowsCount*100);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(getAddUnitButton()));
    }

    public WebElement getAddUnitButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Добавить')]"));
    }

    public List<UnitRow> getUnitRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(UnitRow::new).collect(Collectors.toList());
    }

    public NewUnitPage(WebDriver driver) {
        this.driver = driver;
    }
}
