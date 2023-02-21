package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpellRow {
    private final WebElement row;

    public void selectRow() {
        row.findElement(By.tagName("td")).click();
    }

    public String getName() {
        return (row.findElements(By.tagName("td")).get(0).getText());
    }
    public String getMagicSchool() {
        return (row.findElements(By.tagName("td")).get(1).getText());
    }

    public String getLevel() {
        return (row.findElements(By.tagName("td")).get(2).getText());
    }

    public SpellRow(WebElement row) {
        this.row = row;
    }
}
