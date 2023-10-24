import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.*;

public class LoginUserTest {
    UserClient userClient;
    User user;
    Credentials creds;
    UserAssertions check;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserAssertions();
    }

    @Test
    public void loginUserWithCorrectCredsReturnsSC200() {
        user = UserGenerator.getRandomDefaultUser();
        creds = Credentials.from(user);

        userClient.create(user);
        ValidatableResponse response = userClient.login(creds);
        accessToken = response.extract().path("accessToken");

        check.createdOrLoggedInSuccessfullySC200(response);

        response = userClient.delete(accessToken);
        check.deletedSuccessfullySC202(response);
    }

    @Test
    public void loginUserWithNullEmailReturns401() {
        creds = new Credentials(null, "P@ssw0rd");
        ValidatableResponse response = userClient.login(creds);
        check.notLoggedInWithoutRequiredFieldsSC401(response);
    }

    @Test
    public void loginUserWithNullPasswordReturns401() {
        creds = new Credentials("someemail@ya.ru", null);
        ValidatableResponse response = userClient.login(creds);
        check.notLoggedInWithoutRequiredFieldsSC401(response);
    }

}
