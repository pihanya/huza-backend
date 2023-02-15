//package ru.huza.selenium.unit;
//
//import org.hamcrest.CoreMatchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.support.PageFactory;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import ru.huza.selenium.pages.LoginPage;
//import ru.huza.selenium.util.SeleniumTest;
//import ru.huza.selenium.util.SeleniumTestBase;
//
//
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import static org.mockito.Mockito.doReturn;
//
//@SeleniumTest
//public class LoginPageTests extends SeleniumTestBase {
//    @MockBean
//    JwtAuthenticationController jwtAuthenticationController;
//
//    private LoginPage loginPage;
//    private String loginPageUrl;
//
//    @BeforeEach
//    public void setUp() {
//        this.get("/login");
//        loginPage = PageFactory.initElements(driver, LoginPage.class);
//
//        loginPage.getLoginEmailInput().clear();
//        loginPage.getLoginPasswordInput().clear();
//
//        loginPageUrl = driver.getCurrentUrl();
//    }
//
//    @Test
//    public void loginSuccess() {
//        String email = "email@example.com";
//        String password = "password";
//
//        doReturn(ResponseEntity.ok(new AuthResponse("token")))
//                .when(jwtAuthenticationController)
//                .login(new AuthRequest(null, email, password));
//
//
//        loginPage.getLoginEmailInput().sendKeys(email);
//        loginPage.getLoginPasswordInput().sendKeys(password);
//
//        loginPage.getLoginButton().click();
//        this.redirectWait(loginPageUrl);
//
//        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("/home"));
//    }
//}
