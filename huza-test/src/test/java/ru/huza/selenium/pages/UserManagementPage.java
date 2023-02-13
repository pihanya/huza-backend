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
public class UserManagementPage {
    private final WebDriver driver;

    public void waitUntilUsersLoaded(int number) {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("tr"), number));
    }

    @FindBy(xpath = "//button[contains(text(),'Добавить нового пользователя')]")
    private WebElement addUserButton;

    public List<UserRow> getUserRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(UserRow::new).collect(Collectors.toList());
    }

    public UserManagementPage(WebDriver driver) {this.driver = driver;}
}
