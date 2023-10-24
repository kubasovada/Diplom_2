package user;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class UserClient extends Client {

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";

    public ValidatableResponse create(User user) {
        return spec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    public ValidatableResponse delete(String accessToken) {
        return given().log().all()
                .contentType(JSON)
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .basePath(BASE_PATH + "user")
                .delete()
                .then().log().all();
    }

    public ValidatableResponse login(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
}
