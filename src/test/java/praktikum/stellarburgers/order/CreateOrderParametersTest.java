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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.stellarburgers.constants.UserStatus;
import praktikum.stellarburgers.user.UserClient;
import praktikum.stellarburgers.user.UserRegistrationData;
import praktikum.stellarburgers.user.UserSuccessInfo;

import java.util.Objects;

import static praktikum.stellarburgers.constants.UserStatus.ANONYMOUS_USER;
import static praktikum.stellarburgers.constants.UserStatus.AUTHORIZED_USER;
import static praktikum.stellarburgers.order.OrderDataGenerator.getRandomAccessToken;
import static praktikum.stellarburgers.order.OrderDataGenerator.getRandomOrderData;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

@RunWith(Parameterized.class)
public class CreateOrderParametersTest {
    SoftAssertions softAssertions;

    private final static String NO_ORDER_DATA = "no order data";
    private final static String REAL_ORDER_DATA = "real order data";
    private final static String FAKED_ORDER_DATA = "faked order data";

    private final static String NO_ACCESS_TOKEN = "no access token";
    private final static String EMPTY_TOKEN = "empty token";
    private final static String REAL_ACCESS_TOKEN = "real access token";
    private final static String FAKED_ACCESS_TOKEN = "faked access token";
    private final static String WRONG_FORMAT_ACCESS_TOKEN = "wrong format access token";
    private final static String BROKEN_ACCESS_TOKEN = "broken access token";

    private final static boolean SUCCESS_IS_TRUE = true;
    private final static boolean SUCCESS_IS_FALSE = false;

    private final static boolean FROM_REAL_DATA = true;
    private final static boolean FROM_FAKED_DATA = false;

    private UserClient userClient;
    private UserSuccessInfo userSuccessInfo;
    private OrderClient orderClient;

    private OrderData orderData;
    private String accessToken;
    private String refreshToken;

    private final UserStatus USER_STATUS;
    private final String ORDER_DATA;
    private final String ACCESS_TOKEN;
    private final int EXPECTED_STATUS_CODE;
    private final boolean EXPECTED_SUCCESS;
    private final String EXPECTED_MESSAGE;

    public CreateOrderParametersTest(UserStatus userStatus, String orderData, String accessToken,
                                     int expectedStatusCode, boolean expectedSuccess, String expectedMessage) {
        this.USER_STATUS = userStatus;
        this.ORDER_DATA = orderData;
        this.ACCESS_TOKEN = accessToken;
        this.EXPECTED_STATUS_CODE = expectedStatusCode;
        this.EXPECTED_SUCCESS = expectedSuccess;
        this.EXPECTED_MESSAGE = expectedMessage;
    }

    @Parameterized.Parameters(name="createOrder ( 'userStatus': {0}, 'orderData': {1}, 'accessToken': {2} )")
    public static Object[][] getTestData() {
        return new Object[][]{
                { AUTHORIZED_USER, NO_ORDER_DATA, EMPTY_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "Ingredient ids must be provided" },
                { AUTHORIZED_USER, REAL_ORDER_DATA, EMPTY_TOKEN, SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, FAKED_ORDER_DATA, EMPTY_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "One or more ids provided are incorrect" },

                { AUTHORIZED_USER, NO_ORDER_DATA, NO_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "Ingredient ids must be provided" },
                { AUTHORIZED_USER, REAL_ORDER_DATA, NO_ACCESS_TOKEN, SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, FAKED_ORDER_DATA, NO_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "One or more ids provided are incorrect" },

                { AUTHORIZED_USER, NO_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { AUTHORIZED_USER, REAL_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { AUTHORIZED_USER, FAKED_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },

                { AUTHORIZED_USER, NO_ORDER_DATA, REAL_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "Ingredient ids must be provided" },
                { AUTHORIZED_USER, REAL_ORDER_DATA, REAL_ACCESS_TOKEN, SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, FAKED_ORDER_DATA, REAL_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "One or more ids provided are incorrect" },

                { AUTHORIZED_USER, NO_ORDER_DATA, WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid signature" },
                { AUTHORIZED_USER, REAL_ORDER_DATA,  WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid signature" },
                { AUTHORIZED_USER, FAKED_ORDER_DATA,  WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid signature" },

                { AUTHORIZED_USER, NO_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },
                { AUTHORIZED_USER, REAL_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },
                { AUTHORIZED_USER, FAKED_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },

                { ANONYMOUS_USER, NO_ORDER_DATA, EMPTY_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "Ingredient ids must be provided" },
                { ANONYMOUS_USER, NO_ORDER_DATA, NO_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "Ingredient ids must be provided" },
                { ANONYMOUS_USER, NO_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, NO_ORDER_DATA, WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, NO_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },

                { ANONYMOUS_USER, REAL_ORDER_DATA, EMPTY_TOKEN, SC_OK, SUCCESS_IS_TRUE, null },
                { ANONYMOUS_USER, REAL_ORDER_DATA, NO_ACCESS_TOKEN, SC_OK, SUCCESS_IS_TRUE, null },
                { ANONYMOUS_USER, REAL_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, REAL_ORDER_DATA, WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, REAL_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },

                { ANONYMOUS_USER, FAKED_ORDER_DATA, EMPTY_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "One or more ids provided are incorrect" },
                { ANONYMOUS_USER, FAKED_ORDER_DATA, NO_ACCESS_TOKEN, SC_BAD_REQUEST, SUCCESS_IS_FALSE, "One or more ids provided are incorrect" },
                { ANONYMOUS_USER, FAKED_ORDER_DATA, FAKED_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, FAKED_ORDER_DATA, WRONG_FORMAT_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "invalid token" },
                { ANONYMOUS_USER, FAKED_ORDER_DATA, BROKEN_ACCESS_TOKEN, SC_FORBIDDEN, SUCCESS_IS_FALSE, "jwt malformed" },
        };
    }

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        orderClient = new OrderClient();

        userClient = new UserClient();
        UserRegistrationData userRegistrationData = getRandomUserRegistrationData();

        if (Objects.equals(USER_STATUS, AUTHORIZED_USER)) {
            userClient.createUser(userRegistrationData);
            userSuccessInfo = userClient.loginUser(getCredentialsFrom(userRegistrationData))
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            accessToken = userSuccessInfo.getAccessToken();
            refreshToken = userSuccessInfo.getRefreshToken();
        }
    }

    @After
    public void cleanUp() {
        if (!Objects.equals(userSuccessInfo, null)) {
            if (!Objects.equals(accessToken, null) && (!accessToken.isEmpty())) {
                userClient.logoutUser(refreshToken);
                userClient.deleteUser(accessToken);
            }
        }
    }

    @Epic(value = "Order Client")
    @Feature(value = "operations")
    @Story(value = "createOrder")
    @Test
    @DisplayName("Create order for specific user with specified set of parameters")
    @Description("Check that all incorrect parameter sets generate corresponding errors")
    public void CreateOrderForSpecificUserWithSpecifiedSetOfParameters() {

        switch (ORDER_DATA) {
            case REAL_ORDER_DATA: orderData = getRandomOrderData(FROM_REAL_DATA); break;
            case FAKED_ORDER_DATA: orderData = getRandomOrderData(FROM_FAKED_DATA); break;
            case NO_ORDER_DATA: orderData = null; break;
        }

        switch (ACCESS_TOKEN) {
            case REAL_ACCESS_TOKEN: accessToken = userSuccessInfo.getAccessToken(); break;
            case FAKED_ACCESS_TOKEN: accessToken = getRandomAccessToken(); break;
            case WRONG_FORMAT_ACCESS_TOKEN: accessToken = Objects.equals(USER_STATUS, AUTHORIZED_USER)
                    ? userSuccessInfo.getAccessToken() + "wrongPart"
                    : getRandomAccessToken() + "wrongPart"; break;
            case BROKEN_ACCESS_TOKEN: accessToken = Objects.equals(USER_STATUS, AUTHORIZED_USER)
                    ? userSuccessInfo.getAccessToken().replace(".", "")
                    : getRandomAccessToken().replace(".", "") ; break;
            case NO_ACCESS_TOKEN: accessToken = null; break;
            case EMPTY_TOKEN: accessToken = ""; break;
        }

        ValidatableResponse response = orderClient.createOrder(orderData, accessToken);
        int actualStatusCode = response.extract().statusCode();
        boolean actualSuccess;
        String actualMessage;

        if (actualStatusCode == SC_OK) {
            CreateOrderSuccessInfo createOrderSuccessInfo = response
                    .extract()
                    .body()
                    .as(CreateOrderSuccessInfo.class);
            actualSuccess = createOrderSuccessInfo.isSuccess();
            actualMessage = null;
        } else {
            CreateOrderFailureInfo createOrderFailureInfo = response
                    .extract()
                    .body()
                    .as(CreateOrderFailureInfo.class);
            actualSuccess = createOrderFailureInfo.isSuccess();
            actualMessage = createOrderFailureInfo.getMessage();
        }

        softAssertions.assertThat(actualStatusCode).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(actualSuccess).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
