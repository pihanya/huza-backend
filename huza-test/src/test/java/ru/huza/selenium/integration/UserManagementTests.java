package ru.huza.selenium.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import ru.huza.selenium.pages.*;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.List;

@SeleniumTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagementTests extends SeleniumTestBase {
    @Test
    public void test() throws AWTException, InterruptedException, URISyntaxException {
        this.driver.get(this.composeUrl("/"));

        final String adminEmail = "admin@itmo.ru", adminPass = "password";
        this.authenticate(adminEmail, adminPass);

        this.get("/home");
        final var adminHomePage = this.initPage(AdminHomePage.class);
        String homePageUrl = driver.getCurrentUrl();
        adminHomePage.getUserManagementCard().click();
        this.redirectWait(homePageUrl);

        String userManagementPageUrl = driver.getCurrentUrl();
        final var userManagementPage = this.initPage(UserManagementPage.class);
        List<UserRow> userRows = userManagementPage.getUserRows();
        int expectedRowsCount = userRows.size() + 1;
        userManagementPage.getAddUserButton().click();
        this.redirectWait(userManagementPageUrl);

        String newUserPageUrl = driver.getCurrentUrl();
        final var newUserPage = this.initPage(NewUserPage.class);
        String username = "newuser", email = "newuser@itmo.ru", password = "password", role = "Волшебник";

        newUserPage.getUsernameInput().sendKeys(username);
        newUserPage.getUserEmailInput().sendKeys(email);
        newUserPage.getUserPasswordInput().sendKeys(password);
        newUserPage.editRole(role);

//        newUserPage.getImgInput().click();
//        this.uploadImg("images/testpic.jpg"); //todo: move to constants

        newUserPage.getCreateButton().submit();

        this.redirectWait(newUserPageUrl);

        userRows = userManagementPage.getUserRows();
        UserRow newUserRow = userRows.stream()
                .filter(userRow -> userRow.getEmail().equals(email))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(userRows.size(), expectedRowsCount);
        Assertions.assertNotNull(newUserRow);

        driver.findElement(By.xpath("//tr//td[contains(text(),'" + email + "')]")).click();
        this.redirectWait(userManagementPageUrl);

        String editUserPageUrl = driver.getCurrentUrl();
        final var editUserPage = this.initPage(EditUserPage.class);
        String editedUsername = "editednewuser", editedEmail = "editednewuser@itmo.ru", editedRole = "Строитель";
        editUserPage.getUsernameInput().clear();
        editUserPage.getUserEmailInput().clear();
        editUserPage.getUserPasswordInput().clear();
        editUserPage.getUsernameInput().sendKeys(editedUsername);
        editUserPage.getUserEmailInput().sendKeys(editedEmail);
        editUserPage.getUserPasswordInput().sendKeys(password);
        editUserPage.editRole(editedRole);
        driver.findElement(By.xpath("//button[contains(text(),'Создать')]")).submit();
        this.redirectWait(editUserPageUrl);

        userRows = userManagementPage.getUserRows();
        UserRow editedUserRow = userRows.stream()
                .filter(userRow -> userRow.getEmail().equals(editedEmail))
                .findAny()
                .orElse(null);
        Assertions.assertEquals(userRows.size(), expectedRowsCount);
        Assertions.assertNotNull(editedUserRow);

        driver.findElement(By.xpath("//tr//td[contains(text(),'" + editedEmail + "')]")).click();
        this.redirectWait(userManagementPageUrl);

        editUserPageUrl = driver.getCurrentUrl();
        editUserPage.getDeleteButton().click();
        driver.findElement(By.xpath("//button[contains(text(),'Удалить!')]")).click();
        this.redirectWait(editUserPageUrl);

        userRows = userManagementPage.getUserRows();
        Assertions.assertEquals(userRows.size(), expectedRowsCount - 1);
    }
}
