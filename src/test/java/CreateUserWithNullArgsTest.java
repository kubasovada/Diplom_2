import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAssertions;
import user.UserClient;


public class CreateUserWithNullArgsTest {
    private UserClient userClient;
    private User user;
    private UserAssertions check;

    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserAssertions();
    }

    @Test
    @DisplayName("unsuccessful create user without password")
    public void userCantBeCreatedWithoutPassword() {
        user = new User("some@ya.ru", null, "Marvin");

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFieldsSC403(response);
    }

    @Test
    @DisplayName("unsuccessful create user without email")
    public void userCantBeCreatedWithoutEmail() {
        user = new User(null, "P@ssw0rd", "Marvin");

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFieldsSC403(response);
    }

    @Test
    @DisplayName("unsuccessful create user without name")
    public void userCantBeCreatedWithoutName() {
        user = new User("some@ya.ru", "P@ssw0rd", null);

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFieldsSC403(response);
    }

}
