package ru.huza.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AssetDefsPage {

    private final WebDriver driver;

    public AssetDefsPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//button[contains(text(),'Добавить определение актива')]")
    private WebElement addAssetDefButton;


    public List<AssetDefRow> getAssetDefRows() {
        return this.driver.findElements(By.xpath("//tbody//tr")).stream()
                .map(AssetDefRow::new).collect(Collectors.toList());
    }

}
