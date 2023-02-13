package ru.huza.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import ru.huza.dto.UserDto;
import ru.huza.selenium.pages.UserManagementPage;
import ru.huza.selenium.pages.UserRow;
import ru.huza.selenium.util.SeleniumTest;
import ru.huza.selenium.util.SeleniumTestBase;
import ru.huza.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@SeleniumTest
@Sql(value = {"/initScripts/create-admin.sql", "/initScripts/create-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagementPageTests extends SeleniumTestBase {

    @SpyBean
    UserService userService;

    UserDto userDto1=new UserDto(1, "testuser1@mail.ru", "testuser1", "password", "wizard");
    UserDto userDto2=new UserDto(2, "testuser2@mail.ru", "testuser2", "password", "builder");
    UserDto userDto3=new UserDto(3, "testuser3@mail.ru", "testuser3", "password", "warrior");
    private UserManagementPage userManagementPage;

    private String userManagementPageUrl;

    private boolean authenticated = false;

    private List<UserDto> expectedUsers;

    @BeforeEach
    public void setUp() {
        if (!authenticated) {
            this.authenticate("testadmin@itmo.ru", "password");
            authenticated = true;
        }
        expectedUsers = List.of(userDto1, userDto2, userDto3);
        doReturn(expectedUsers).when(userService).findAll();
        this.get("/home/users");
        userManagementPage = PageFactory.initElements(driver, UserManagementPage.class);
        userManagementPageUrl = driver.getCurrentUrl();
    }

    @Test
    public void showUsers() {
        userManagementPage.waitUntilUsersLoaded(expectedUsers.size());
        List<UserRow> userRows = userManagementPage.getUserRows();
        List<Integer> expectedIds = List.of(1,2,3);
        Assertions.assertEquals(expectedIds, userRows.stream().map(UserRow::getId).collect(Collectors.toList()));
        List<String> expectedEmails = List.of("testuser1@mail.ru","testuser2@mail.ru","testuser3@mail.ru");
        Assertions.assertEquals(expectedEmails, userRows.stream().map(UserRow::getEmail).collect(Collectors.toList()));
        List<String> expectedRoles = List.of("wizard","builder","warrior");
        Assertions.assertEquals(expectedRoles, userRows.stream().map(UserRow::getRole).collect(Collectors.toList()));
    }
    @Test
    public void editUserMail() {
        userManagementPage.waitUntilUsersLoaded(expectedUsers.size());
        List<UserRow> userRows = userManagementPage.getUserRows();
        UserRow userRow = userRows.get(0);
        String expectedEmail = "edited@mail.ru";

        UserDto editedUserDto1 = new UserDto(userDto1.getId(), expectedEmail, userDto1.getUsername(), userDto1.getPassword(), userDto1.getRole());

        doReturn(editedUserDto1).when(userService).save(ArgumentMatchers.any());
        doReturn(List.of(editedUserDto1, userDto2, userDto3)).when(userService).findAll();

        userRow.editEmail(expectedEmail);
        Assertions.assertEquals(userRow.getEmail(), expectedEmail);
    }

    @Test
    public void editUserRole() {
        userManagementPage.waitUntilUsersLoaded(expectedUsers.size());
        List<UserRow> userRows = userManagementPage.getUserRows();
        UserRow userRow = userRows.get(0);
        String expectedRole = "owner";

        UserDto editedUserDto1 = new UserDto(userDto1.getId(), userDto1.getEmail(), userDto1.getUsername(), userDto1.getPassword(), expectedRole);

        doReturn(editedUserDto1).when(userService).save(ArgumentMatchers.any());
        doReturn(List.of(editedUserDto1, userDto2, userDto3)).when(userService).findAll();

        userRow.editRole();
        Assertions.assertEquals(userRow.getRole(), expectedRole);
    }

    @Test
    public void redirectToNewUserForm() {
        userManagementPage.getAddUserButton().click();
        this.redirectWait(userManagementPageUrl);
        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("/home/users/new"));
    }

//    @Test
//    public void deleteUser() {
//        userManagementPage.waitUntilUsersLoaded(expectedUsers.size());
//        List<UserRow> userRows = userManagementPage.getUserRows();
//        UserRow userRow = userRows.get(0);
//
//        doReturn(true).when(userService).removeById(1);
//        doReturn(List.of(userDto2, userDto3)).when(userService).findAll();
//
//        userRow.getDeleteButton().click();
//        List<Integer> expectedIds = List.of(2,3);
//
//        userManagementPage.waitUntilUsersLoaded(expectedIds.size());
//
//        Assertions.assertEquals(expectedIds, userManagementPage.getUserRows().stream().map(UserRow::getId).collect(Collectors.toList()));
//    }
}
