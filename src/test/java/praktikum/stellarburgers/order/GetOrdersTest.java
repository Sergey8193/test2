package praktikum.stellarburgers.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.stellarburgers.user.UserClient;
import praktikum.stellarburgers.user.UserRegistrationData;
import praktikum.stellarburgers.user.UserSuccessInfo;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.SoftAssertions;

import static praktikum.stellarburgers.order.OrderDataGenerator.getRandomOrderData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class GetOrdersTest {
    private SoftAssertions softAssertions;
    private final static String NO_ACCESS_TOKEN = null;
    private final static boolean FROM_REAL_DATA = true;
    private UserClient userClient;
    private UserSuccessInfo userSuccessInfo;
    private OrderClient orderClient;
    private String accessToken;
    private CreateOrderSuccessInfo createOrderSuccessInfo;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        orderClient = new OrderClient();
        userClient = new UserClient();
        UserRegistrationData userRegistrationData = getRandomUserRegistrationData();
        userClient.createUser(userRegistrationData);
        userSuccessInfo = userClient
                .loginUser(getCredentialsFrom(userRegistrationData))
                .extract()
                .body()
                .as(UserSuccessInfo.class);
        OrderData orderData = getRandomOrderData(FROM_REAL_DATA);
        accessToken = userSuccessInfo.getAccessToken();
        ValidatableResponse response = orderClient.createOrder(orderData, accessToken);
        createOrderSuccessInfo = response.extract().body().as(CreateOrderSuccessInfo.class);
    }

    @After
    public void cleanUp() {
        if (!Objects.equals(userSuccessInfo, null)) {
            if (!Objects.equals(userSuccessInfo.getAccessToken(), null) &&
                    (!userSuccessInfo.getAccessToken().isEmpty())) {
                userClient.logoutUser(userSuccessInfo.getRefreshToken());
                userClient.deleteUser(userSuccessInfo.getAccessToken());
            }
        }
    }

    @Epic(value = "Order Client")
    @Feature(value = "operations")
    @Story(value = "getOrders")
    @Test
    @DisplayName("Get orders for logged in user")
    @Description("Check that logged in user can receive his own order list")
    public void getOrdersForLoggedInUser() {
        ValidatableResponse response = orderClient.getOrders(accessToken);
        int statusCode = response.extract().statusCode();
        boolean success = response.extract().body().path("success");

        softAssertions.assertThat(statusCode).isEqualTo(SC_OK);
        softAssertions.assertThat(success).isEqualTo(true);
        softAssertions.assertAll();

        if (statusCode == SC_OK) {
            GetOrdersSuccessInfo getOrdersSuccessInfo = response.extract().body().as(GetOrdersSuccessInfo.class);
            List<GetOrdersOrderData> orders = getOrdersSuccessInfo.getOrders();

            Assert.assertFalse(orders.isEmpty());

            if (!orders.isEmpty()) {
                String expectedId = createOrderSuccessInfo.getOrder().get_id();
                String actualId = getOrdersSuccessInfo.getOrders().get(0).get_id();

                int expectedNumber = createOrderSuccessInfo.getOrder().getNumber();
                int actualNumber = getOrdersSuccessInfo.getOrders().get(0).getNumber();

                String expectedName = createOrderSuccessInfo.getOrder().getName();
                String actualName = getOrdersSuccessInfo.getOrders().get(0).getName();

                softAssertions.assertThat(orders.size()).isEqualTo(1);
                softAssertions.assertThat(actualId).isEqualTo(expectedId);
                softAssertions.assertThat(actualNumber).isEqualTo(expectedNumber);
                softAssertions.assertThat(actualName).isEqualTo(expectedName);
                softAssertions.assertAll();
            }
        }
    }

    @Epic(value = "Order Client")
    @Feature(value = "operations")
    @Story(value = "getOrders")
    @Test
    @DisplayName("Get orders for anonymous user")
    @Description("Check that anonymous user can not receive some order list")
    public void getOrdersForAnonymousUser() {
        ValidatableResponse response = orderClient.getOrders(NO_ACCESS_TOKEN);
        response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
}
