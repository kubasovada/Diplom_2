import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAssertions;
import user.UserClient;
import user.UserGenerator;

import java.net.HttpURLConnection;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;

public class ChangeUserTest {
    UserClient userClient;
    User user;
    String accessToken;
    UserAssertions check;

    private static final Random rnd = new Random();
    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserAssertions();
    }

    @After
    public void tearDown() {
        ValidatableResponse response = userClient.delete(accessToken);
        check.deletedSuccessfullySC202(response);
    }

    @Test
    @DisplayName("change name for authorized user")
    public void changeNameSuccessTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newName = user.getName()+"ChangedName";
        user.setName(newName);

        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true),"user.name", is(newName));

    }

    @Test
    @DisplayName("change password for authorized user")
    public void changePasswordSuccessTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newPsw = user.getPassword()+ rnd.nextInt(10000);
        user.setPassword(newPsw);
        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true), "user.email", is(user.getEmail().toLowerCase()));
    }
    @Test
    @DisplayName("change email for authorized user")
    public void changeEmailSuccessTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newEmail = rnd.nextInt(10000)+user.getEmail();
        user.setEmail(newEmail);
        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true), "user.email", is(user.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("change email for existed email for authorized user")
    public void changeEmailUnsuccessTestSC403() {
        user = UserGenerator.getRandomDefaultUser();
        User anotherUser = UserGenerator.getRandomDefaultUser();

        ValidatableResponse response = userClient.create(user);
        ValidatableResponse responseOfAnotherUser = userClient.create(anotherUser); //создаю, чтобы взять существующий email
        accessToken = response.extract().path("accessToken");
        String accessTokenOfAnotherUser = responseOfAnotherUser.extract().path("accessToken"); // для последующего удаления
        String emailOfAnotherExistedUser = anotherUser.getEmail();
        user.setEmail(emailOfAnotherExistedUser);

        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false), "message", is("User with such email already exists"));

        responseOfAnotherUser = userClient.delete(accessTokenOfAnotherUser);
        check.deletedSuccessfullySC202(responseOfAnotherUser);
    }

    @Test
    @DisplayName("change name for unauthorized user")
    public void changeNameWithoutTokenTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setName(user.getName()+"ChangedName");
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutTokenSC401(response);

    }

    @Test
    @DisplayName("change email for unauthorized user")
    public void changeEmailWithoutTokenTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail("newEmail"+user.getEmail());
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutTokenSC401(response);
    }

    @Test
    @DisplayName("change password for unauthorized user")
    public void changePasswordWithoutTokenTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setPassword(user.getPassword()+"newPsw");
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutTokenSC401(response);
    }

}
