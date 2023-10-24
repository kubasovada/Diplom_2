package user;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class Client {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected static final String BASE_PATH = "/api/auth/";

    public RequestSpecification spec() {
        return given().log().all()
                .contentType(JSON)
                .baseUri(BASE_URI)
                .basePath(BASE_PATH);
    }
}
