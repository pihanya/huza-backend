package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WarriorHomePage {
    private final WebDriver driver;

    public void waitUntilButtonLoaded(int rowsCount, WebElement button) throws InterruptedException {
        Thread.sleep(rowsCount*100);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(button));
    }

    public List<UnitRow> getUnitRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(UnitRow::new).collect(Collectors.toList());
    }

    public WebElement getAddUnitButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Добавить нового рекрута')]"));
    }

    public WebElement getDeleteButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Удалить')]"));
    }


    public WebElement getEditInput() {
        return this.driver.findElement(By.id("newValue"));
    }

    public WebElement getSaveButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Сохранить')]"));
    }


    public WebElement getEditButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Изменить')]"));
    }

    public WarriorHomePage(WebDriver driver) {
        this.driver = driver;
    }
}
