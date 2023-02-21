package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class LoginPage {
    private final WebDriver driver;

    @FindBy(id = "login_email")
    private WebElement loginEmailInput;

    @FindBy(id = "login_password")
    private WebElement loginPasswordInput;

    public WebElement getInvalidFeedback() {
        return this.driver.findElement(By.className("invalid-feedback"));
    }

    @FindBy(xpath = "//button[contains(text(),'Войти')]")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
}
