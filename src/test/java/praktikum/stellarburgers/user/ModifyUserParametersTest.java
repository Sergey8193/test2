package praktikum.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import praktikum.stellarburgers.constants.UserStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static praktikum.stellarburgers.constants.UserStatus.ANONYMOUS_USER;
import static praktikum.stellarburgers.constants.UserStatus.AUTHORIZED_USER;
import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.*;

@RunWith(Parameterized.class)
public class ModifyUserParametersTest {
    SoftAssertions softAssertions;

    private final static boolean SUCCESS_IS_TRUE = true;
    private final static boolean SUCCESS_IS_FALSE = false;

    private UserClient userClient;
    private UserRegistrationData userRegistrationData;
    private UserSuccessInfo userSuccessInfo;

    private String refreshToken;
    private String accessToken;

    private final UserStatus USER_STATUS;
    private final String NAME;
    private final String EMAIL;
    private final String PASSWORD;
    private final int EXPECTED_STATUS_CODE;
    private final boolean EXPECTED_SUCCESS;
    private final String EXPECTED_MESSAGE;

    private final static String THE_SAME_NAME = "the same";
    private final static String THE_SAME_EMAIL = "the same";
    private final static String THE_SAME_PASSWORD = "the same";

    public ModifyUserParametersTest(UserStatus userStatus,
                                    String name, String email, String password,
                                    int expectedStatusCode, boolean expectedSuccess, String expectedMessage) {
        this.USER_STATUS = userStatus;
        this.NAME = name;
        this.EMAIL =  email;
        this.PASSWORD = password;
        this.EXPECTED_STATUS_CODE = expectedStatusCode;
        this.EXPECTED_SUCCESS = expectedSuccess;
        this.EXPECTED_MESSAGE = expectedMessage;
    }

    @Parameterized.Parameters(name="modifyUser ( 'userStatus': {0}, 'name': {1}, 'email': {2}, 'password': {3} )")
    public static Object[][] getTestData() {
        return new Object[][]{

                { AUTHORIZED_USER, getRandomName(), getRandomEmail(), getRandomPassword(), SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, getRandomName(), getRandomEmail(), THE_SAME_PASSWORD, SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, getRandomName(), THE_SAME_EMAIL, getRandomPassword(), SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, getRandomName(), THE_SAME_EMAIL, THE_SAME_PASSWORD, SC_OK, SUCCESS_IS_TRUE, null },

                { AUTHORIZED_USER, THE_SAME_NAME, getRandomEmail(), getRandomPassword(), SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, THE_SAME_NAME, getRandomEmail(), THE_SAME_PASSWORD, SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, THE_SAME_NAME, THE_SAME_EMAIL, getRandomPassword(), SC_OK, SUCCESS_IS_TRUE, null },
                { AUTHORIZED_USER, THE_SAME_NAME, THE_SAME_EMAIL, THE_SAME_PASSWORD, SC_OK, SUCCESS_IS_TRUE, null },

                { ANONYMOUS_USER, getRandomName(), getRandomEmail(), getRandomPassword(), SC_UNAUTHORIZED, SUCCESS_IS_FALSE, "You should be authorised" },
        };
    }

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();

        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();

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
            if (!Objects.equals(userSuccessInfo.getAccessToken(), null) &&
                    (!userSuccessInfo.getAccessToken().isEmpty())) {
                userClient.logoutUser(refreshToken);
                userClient.deleteUser(accessToken);
            }
        }
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "modifyUser")
    @Test
    @DisplayName("Modify user with correct user data")
    @Description("Modify user with correct 'Name', 'Login' and 'Password'")
    public void modifyUserWithCorrectUserData() {

        if (!Objects.equals(EMAIL, THE_SAME_EMAIL)) userRegistrationData.setEmail(EMAIL);
        if (!Objects.equals(PASSWORD, THE_SAME_PASSWORD)) userRegistrationData.setPassword(PASSWORD);
        if (!Objects.equals(NAME, THE_SAME_NAME)) userRegistrationData.setName(NAME);

        ValidatableResponse response = userClient.modifyUser(userRegistrationData, accessToken);
        int actualStatusCode = response.extract().statusCode();
        boolean actualSuccess;
        String actualMessage;

        if (actualStatusCode == SC_OK) {
            UserSuccessInfo userSuccessInfo = response
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            actualSuccess = userSuccessInfo.isSuccess();
            actualMessage = null;
        } else {
            UserFailureInfo userFailureInfo = response
                    .extract()
                    .body()
                    .as(UserFailureInfo.class);
            actualSuccess = userFailureInfo.isSuccess();
            actualMessage = userFailureInfo.getMessage();
        }

        softAssertions.assertThat(actualStatusCode).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(actualSuccess).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
