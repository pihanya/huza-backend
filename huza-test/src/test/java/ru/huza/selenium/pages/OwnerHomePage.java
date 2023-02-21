package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class OwnerHomePage {
    private final WebDriver driver;

    @FindBy(xpath = "//div//*[contains(text(),'Строительство')]")
    private WebElement buildCard;

    @FindBy(xpath = "//div//*[contains(text(),'Рекруты')]")
    private WebElement recruitCard;

    @FindBy(xpath = "//div//*[contains(text(),'Ресурсы')]")
    private WebElement resourcesCard;

    @FindBy(xpath = "//div//*[contains(text(),'Здания')]")
    private WebElement buildingsCard;

    public OwnerHomePage(WebDriver driver) {
        this.driver = driver;
    }
}
