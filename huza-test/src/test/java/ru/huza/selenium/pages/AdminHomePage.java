package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class AdminHomePage {
    private final WebDriver driver;

    @FindBy(xpath = "//div//*[contains(text(),'Управление пользователями')]")
    private WebElement userManagementCard;

    @FindBy(xpath = "//div//*[contains(text(),'Сбор аудитных данных по истории изменения активов')]")
    private WebElement auditCard;

    @FindBy(xpath = "//div//*[contains(text(),'Активы замка')]")
    private WebElement resourcesCard;

    public AdminHomePage(WebDriver driver) {
        this.driver = driver;
    }
}
