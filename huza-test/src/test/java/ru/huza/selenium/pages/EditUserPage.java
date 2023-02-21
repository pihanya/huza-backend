package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class EditUserPage {
    private final WebDriver driver;

    @FindBy(id = "user_name")
    private WebElement usernameInput;

    @FindBy(id = "user_email")
    private WebElement userEmailInput;

    @FindBy(id = "user_password")
    private WebElement userPasswordInput;

    @FindBy(id = "user_role")
    private WebElement roleSelect;

    public void editRole(String role) {
        roleSelect.click();
        driver.findElement(By.xpath("//option[contains(text(),'" + role + "')]")).click();
    }

    @FindBy(xpath = "//button[contains(text(),'Удалить')]")
    private WebElement deleteButton;

    public EditUserPage(WebDriver driver) {
        this.driver = driver;
    }
}
