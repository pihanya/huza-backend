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
public class NewSpellPage {

    private final WebDriver driver;

    public void waitUntilSpellsLoaded(int number) throws InterruptedException {
        //Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("tr"), number));
    }
    public void waitUntilAddSpellButtonLoaded(int rowsCount) throws InterruptedException {
        Thread.sleep(rowsCount*100);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(getAddSpellButton()));
    }

    public WebElement getAddSpellButton() {
        return this.driver.findElement(By.xpath("//button[contains(text(),'Добавить')]"));
    }

    public List<SpellRow> getSpellRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(SpellRow::new).collect(Collectors.toList());
    }

    public NewSpellPage(WebDriver driver) {
        this.driver = driver;
    }
}
