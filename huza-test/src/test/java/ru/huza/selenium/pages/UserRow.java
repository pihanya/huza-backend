package ru.huza.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserRow {
    //private final WebDriver driver;

    private final WebElement row;

    public int getId() {
        return Integer.parseInt(row.findElements(By.tagName("td")).get(0).getText());
    }

    public String getEmail() {
        return (row.findElements(By.tagName("td")).get(1).getText());
    }

    public void editEmail(String email) {
        row.findElements(By.tagName("td")).get(1).click();
        row.findElement(By.tagName("input")).clear();
        row.findElements(By.tagName("td")).get(1).click();
        row.findElement(By.tagName("input")).sendKeys(email);
        row.findElement(By.tagName("input")).sendKeys(Keys.ENTER);
    }

    public String getRole() {
        return (row.findElements(By.tagName("td")).get(2).getText());
    }

    public void editRole() {
        row.findElements(By.tagName("td")).get(2).click();
        row.findElements(By.tagName("td")).get(2).click();
        row.findElement(By.xpath("//option[contains(text(),'Владелец замка')]")).click();
    }

    public WebElement getDeleteButton() {
        return row.findElement(By.xpath("//td//div"));
    }

    public UserRow(WebElement row) {
        this.row = row;
    }
}
