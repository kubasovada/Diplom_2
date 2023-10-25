import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAssertions;
import user.UserClient;
import user.UserGenerator;

public class CreateUserTest {
    private UserClient userClient;
    private User user;
    private UserAssertions check = new UserAssertions();
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        ValidatableResponse delete = userClient.delete(accessToken);
        check.deletedSuccessfullySC202(delete);
    }


    @Test
    @DisplayName("create user with valid user data")
    public void userCanBeCreatedTest() {
        user = UserGenerator.getRandomDefaultUser();

        ValidatableResponse response = userClient.create(user);
        check.createdOrLoggedInSuccessfullySC200(response);

        accessToken = response.extract().path("accessToken");
    }


    @Test
    @DisplayName("unsuccessful create user with existed user data")
    public void userCantBeCreatedWithExistingUserTest() {
        user = UserGenerator.getRandomDefaultUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");

        response = userClient.create(user);
        check.notCreatedExistedUserSC403(response);
    }

}
