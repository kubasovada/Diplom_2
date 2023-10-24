package user;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class UserClient extends Client {

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String USER = "user";


    public ValidatableResponse create(User user) {
        return spec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    public ValidatableResponse delete(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .delete(USER)
                .then().log().all();
    }

    public ValidatableResponse login(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }

    public ValidatableResponse changeUser(User user, String accessToken) {
         return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then().log().all();
    }

    public ValidatableResponse changeUserWithoutToken (User user) {
        return spec()
                .body(user)
                .when()
                .patch(USER)
                .then().log().all();
    }
}
