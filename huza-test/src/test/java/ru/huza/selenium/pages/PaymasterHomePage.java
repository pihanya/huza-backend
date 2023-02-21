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
public class PaymasterHomePage {
    private final WebDriver driver;

    public void waitUntilEditButtonLoaded(int rowsCount) throws InterruptedException {
        Thread.sleep(rowsCount*100);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(getEditButton()));
    }

    public List<ResourceRow> getResourceRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(ResourceRow::new).collect(Collectors.toList());
    }

    public WebElement getEditButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Изменить')]"));
    }

    @FindBy(id = "newValue")
    private WebElement newValueInput;

    public WebElement getSaveButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Сохранить')]"));
    }

    public PaymasterHomePage(WebDriver driver) {
        this.driver = driver;
    }
}
