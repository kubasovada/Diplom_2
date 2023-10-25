package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserClient extends Client {

    private static final String REGISTER = "/auth/register";
    private static final String LOGIN = "/auth/login";
    private static final String USER = "/auth/user";


    public ValidatableResponse create(User user) {
        return spec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    @Step("Send DELETE request to /api/auth/user")
    public ValidatableResponse delete(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .delete(USER)
                .then().log().all();
    }
    @Step("Send POST request to /api/auth/login to login")
    public ValidatableResponse login(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
    @Step("Send PATCH request to /api/auth/user to change user data with token")
    public ValidatableResponse changeUser(User user, String accessToken) {
         return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then().log().all();
    }
    @Step("Send PATCH request to /api/auth/user to change user data without token")
    public ValidatableResponse changeUserWithoutToken (User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER)
                .then().log().all();
    }
}
