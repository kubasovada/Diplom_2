package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.Client;

public class OrderClient extends Client {
    private static final String ORDERS = "/orders";

    @Step("Send POST request to /api/orders with token")
    public ValidatableResponse createOrderAuthorizedUser(Order order, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }
    @Step("Send POST request to /api/orders without token")
    public ValidatableResponse createOrderUnauthorizedUser(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Send GET request to /api/orders with token")
    public ValidatableResponse getOrdersAuthorizedUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .get(ORDERS)
                .then().log().all();
    }
    @Step("Send GET request to /api/orders without token")
    public ValidatableResponse getOrdersUnauthorizedUser() {
        return spec()
                .get(ORDERS)
                .then().log().all();
    }


}
