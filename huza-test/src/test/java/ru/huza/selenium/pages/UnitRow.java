package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UnitRow {
    private final WebElement row;

    public void selectRow() {
        row.findElement(By.tagName("td")).click();
    }

    public String getName() {
        return (row.findElements(By.tagName("td")).get(0).getText());
    }
    public String getFraction() {
        return (row.findElements(By.tagName("td")).get(1).getText());
    }

    public String getLevel() {
        return (row.findElements(By.tagName("td")).get(2).getText());
    }

    public String getCount() {
        return (row.findElements(By.tagName("td")).get(4).getText());
    }

    public UnitRow(WebElement row) {
        this.row = row;
    }
}
