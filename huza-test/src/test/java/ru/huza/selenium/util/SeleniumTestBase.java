package ru.huza.selenium.util;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.huza.selenium.pages.LoginPage;

import java.time.Duration;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class SeleniumTestBase {
    @Autowired
    protected WebDriver driver;

    @Autowired
    AuthenticationController jwtAuthenticationController;
    @LocalServerPort
    private final static int port = 3000;


    protected void waitForAngularRequests() {
        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver) driver);
        ngDriver.waitForAngularRequestsToFinish();
    }

    protected void switchToNewWindow(Set<String> oldWindows) {
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(oldWindows.size() + 1));
        var handles = this.driver.getWindowHandles();
        handles.removeAll(oldWindows);

        var newWindow = handles.stream().findFirst().orElseThrow();
        this.driver.switchTo().window(newWindow);
    }

    protected void switchToNewWindow(String sourceWindowHandle) {
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(2));
        var handles = this.driver.getWindowHandles();
        handles.remove(sourceWindowHandle);

        var newWindow = handles.stream().findFirst().orElseThrow();
        this.driver.switchTo().window(newWindow);
    }

    protected void assertUrlMatches(String destination) {
        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(destination));
    }

    protected void assertRedirects(Executable executable, String destination) {
        this.redirectWait(executable);

        this.assertUrlMatches(destination);
    }

    protected void assertNoRedirect(Executable executable) {
        Assertions.assertThrows(Exception.class, () -> this.redirectWait(executable));
    }

    protected void redirectWait(Executable executable) {
        this.redirectWait(executable, Duration.ofSeconds(1));
    }

    protected void redirectWait(Executable executable, Duration timeout) {
        final var sourceUrl = this.driver.getCurrentUrl();

        try {
            executable.execute();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        this.redirectWait(sourceUrl, timeout);
    }

    protected void redirectWait(String originalUrl) {
        redirectWait(originalUrl, Duration.ofSeconds(1));
    }

    protected void redirectWait(String originalUrl, Duration timeout) {
        new WebDriverWait(this.driver, timeout).until(
                ExpectedConditions.not(
                        ExpectedConditions.urlToBe(originalUrl)));
    }

    protected <T> T getInit(String relativeUrl, Class<T> pageClass) {
        this.get(relativeUrl);
        return this.initPage(pageClass);
    }

    protected <T> T initPage(Class<T> pageClass) {
        this.waitForAngularRequests();
        return PageFactory.initElements(this.driver, pageClass);
    }

    protected void get(String relativeUrl) {
        this.driver.get(this.composeUrl(relativeUrl));
    }

    protected String composeUrl(String relativeUrl) {
        return String.format("localhost:%d%s", this.port, relativeUrl);
    }

    protected void authenticate(String email, String password) {
        String loginPageUrl;
        get("/login");
        loginPageUrl = driver.getCurrentUrl();

        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getLoginEmailInput().sendKeys(email);
        loginPage.getLoginPasswordInput().sendKeys(password);

        loginPage.getLoginButton().click();
        redirectWait(loginPageUrl);
    }
}
