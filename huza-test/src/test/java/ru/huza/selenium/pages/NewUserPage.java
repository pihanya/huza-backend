package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class NewUserPage {
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

    @FindBy(xpath = "//div[contains(text(),'Добавить изображение')]")
    private WebElement imgInput;

    @FindBy(xpath = "//button[contains(text(),'Создать')]")
    private WebElement createButton;

    @FindBy(xpath = "//button[contains(text(),'Отмена')]")
    private WebElement cancelButton;

    public NewUserPage(WebDriver driver) {
        this.driver = driver;
    }
}
