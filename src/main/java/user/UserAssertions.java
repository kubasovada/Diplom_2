package user;

import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.is;

public class UserAssertions {
    public void createdOrLoggedInSuccessfullySC200(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true));
    }

    public void deletedSuccessfullySC202(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_ACCEPTED)
                .body("success", is(true));
    }

    public  void notCreatedExistedUserSC403(ValidatableResponse response) {
         response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false), "message", is("User already exists"));
    }

    public  void notCreatedWithoutRequiredFieldsSC403(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", is("Email, password and name are required fields"));
    }

    public  void notLoggedInWithoutRequiredFieldsSC401(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", is("email or password are incorrect"));
    }
}
