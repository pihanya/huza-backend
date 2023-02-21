package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AssetDefRow {

    private final WebElement row;

    public void selectRow() {
        row.findElement(By.tagName("td")).click();
    }

    public AssetDefRow(WebElement row) {
        this.row = row;
    }

    public String getName() {
        return (row.findElements(By.tagName("td")).get(0).getText());
    }

    public String getCategory() {
        return (row.findElements(By.tagName("td")).get(1).getText());
    }

    public String getCost() {
        return (row.findElements(By.tagName("td")).get(2).getText());
    }
}
