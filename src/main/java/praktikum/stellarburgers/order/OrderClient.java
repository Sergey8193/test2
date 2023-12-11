package praktikum.stellarburgers.order;

import praktikum.stellarburgers.constants.RestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Objects;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_URL = BASE_URL + "orders/";

    @Step("GET getOrders {accessToken}")
    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .spec(getRequestSpecification(accessToken))
                .when()
                .get(ORDER_URL)
                .then();
    }

    @Step("POST createOrder {orderData} ( accessToken=\"{accessToken}\" )")
    public ValidatableResponse createOrder(OrderData data, String accessToken) {
        OrderData orderData = Objects.equals(data, null)
                ? new OrderData()
                : data;
        return given()
                .spec(getRequestSpecification(accessToken))
                .body(orderData)
                .when()
                .post(ORDER_URL)
                .then();
    }
}
