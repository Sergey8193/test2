package praktikum.stellarburgers.order;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.stellarburgers.user.UserClient;
import praktikum.stellarburgers.user.UserRegistrationData;
import praktikum.stellarburgers.user.UserSuccessInfo;

import java.util.Objects;

import static praktikum.stellarburgers.order.OrderDataGenerator.getRandomOrderData;
import static org.hamcrest.Matchers.equalTo;
import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class CreateOrderTest {
    SoftAssertions softAssertions;

    private final boolean FROM_REAL_DATA = true;

    private UserClient userClient;
    private UserSuccessInfo userSuccessInfo;
    private OrderClient orderClient;

    OrderData orderData;
    String accessToken;

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
    @Story(value = "createOrder")
    @Test
    @DisplayName("Create order for logged in user")
    @Description("Check that if order can be created by logged in user")
    public void createOrderForLoggedInUser() {
        orderData = getRandomOrderData(FROM_REAL_DATA);
        accessToken = userSuccessInfo.getAccessToken();
        ValidatableResponse response = orderClient.createOrder(orderData, accessToken);
        response.log().all()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        CreateOrderSuccessInfo createOrderSuccessInfo = response.extract().body().as(CreateOrderSuccessInfo.class);
        CreateOrderOrderData order = createOrderSuccessInfo.getOrder();

        softAssertions.assertThat(order.getName()).isNotEqualTo(null);
        softAssertions.assertThat(order.getNumber()).isGreaterThan(0);
        softAssertions.assertThat(order.getPrice()).isGreaterThan(0);
        softAssertions.assertThat(order.getOwner().getName()).isEqualTo(userSuccessInfo.getUser().getName());
        softAssertions.assertThat(order.getOwner().getEmail()).isEqualTo(userSuccessInfo.getUser().getEmail());
        softAssertions.assertAll();
    }

    @Epic(value = "Order Client")
    @Feature(value = "operations")
    @Story(value = "createOrder")
    @Test
    @DisplayName("Create order for anonymous user")
    @Description("Check that if order can be created by anonymous user")
    public void createOrderForAnonymousUser() {
        orderData = getRandomOrderData(FROM_REAL_DATA);
        ValidatableResponse response = orderClient.createOrder(orderData, null);
        response.log().all()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        CreateOrderSuccessInfo createOrderSuccessInfo = response.extract().body().as(CreateOrderSuccessInfo.class);
        CreateOrderOrderData order = createOrderSuccessInfo.getOrder();

        softAssertions.assertThat(createOrderSuccessInfo.getName()).isNotEqualTo(null);
        softAssertions.assertThat(order.getNumber()).isGreaterThan(0);
        softAssertions.assertAll();
    }
}
