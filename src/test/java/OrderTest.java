import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderClient;
import order.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserAssertions;
import user.UserClient;
import user.User;
import user.UserGenerator;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class OrderTest {
    OrderClient orderClient;
    UserClient userClient;
    User user;
    String accessToken;
    Order order;
    private  List<String> orderList = new ArrayList<>();
    UserAssertions check = new UserAssertions();

    @Before
    public void setUp() {
        user = UserGenerator.getRandomDefaultUser();
        userClient = new UserClient();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        ValidatableResponse response = userClient.delete(accessToken);
        check.deletedSuccessfullySC202(response);

    }

    @Test
    @DisplayName("create order with ingredients for authorized user")
    public void createOrderAuthorizedUserWithIngredients(){
        order = new Order();
        orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.createOrderAuthorizedUser(order, accessToken);
        response.assertThat().statusCode(HttpURLConnection.HTTP_OK).body("success", is(true));
    }

    @Test
    @DisplayName("create order without ingredients for authorized user")
    public void createOrderAuthorizedUserWithoutIngredients(){
        order = new Order();
        orderList.clear();
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.createOrderAuthorizedUser(order, accessToken);
        response.assertThat().statusCode(HttpURLConnection.HTTP_BAD_REQUEST).body("success", is(false),
                "message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("create order with ingredients for unauthorized user")
    public void createOrderUnauthorizedUserWithIngredients(){
        order = new Order();
        orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.createOrderUnauthorizedUser(order);
        response.assertThat().statusCode(HttpURLConnection.HTTP_OK).body("success", is(true));
    }

    @Test
    @DisplayName("create order with incorrect ingredients for authorized user")
    public void createOrderAuthorizedUserWithIncorrectIngredients(){
        order = new Order();
        orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d"+"123", "61c0c5a71d1f82001bdaaa6f"+"321"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.createOrderAuthorizedUser(order, accessToken);
        response.assertThat().statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @Test
    @DisplayName("get order with ingredients for authorized user")
    public void getOrdersAuthorizedUserWithIngredients(){
        order = new Order();
        orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.getOrdersAuthorizedUser(accessToken);
        response.assertThat().statusCode(HttpURLConnection.HTTP_OK).body("success", is(true));
    }

    @Test
    @DisplayName("get order with ingredients for unauthorized user")
    public void getOrdersUnauthorizedUserWithIngredients(){
        order = new Order();
        orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderClient.getOrdersUnauthorizedUser();
        response.assertThat().statusCode(HttpURLConnection.HTTP_UNAUTHORIZED).body("success", is(false),
                "message", is("You should be authorised"));
    }

}
