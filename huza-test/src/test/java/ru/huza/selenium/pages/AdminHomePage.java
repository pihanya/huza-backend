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

    @FindBy(xpath = "//div//*[contains(text(),'Определения активов')]")
    private WebElement assetDefCard;

    @FindBy(xpath = "//div//*[contains(text(),'Ресурсы замка')]")
    private WebElement resourcesCard;

    @FindBy(xpath = "//div//*[contains(text(),'Рекруты замка')]")
    private WebElement unitsCard;

    @FindBy(xpath = "//div//*[contains(text(),'Заклинания замка')]")
    private WebElement spellsCard;

    @FindBy(xpath = "//div//*[contains(text(),'Здания замка')]")
    private WebElement buldingsCard;

    public AdminHomePage(WebDriver driver) {
        this.driver = driver;
    }
}
