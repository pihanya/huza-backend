package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BuildOrderRow {
    private final WebElement row;

    public String getBuilding() {
        return (row.findElements(By.tagName("td")).get(0).getText());
    }

    public String getOrder() {
        return (row.findElements(By.tagName("td")).get(1).getText());
    }

    public String getComment() {
        return (row.findElements(By.tagName("td")).get(2).getText());
    }

    public String getStatus() {
        return (row.findElements(By.tagName("td")).get(3).getText());
    }

    public BuildOrderRow(WebElement row) {
        this.row = row;
    }
}
