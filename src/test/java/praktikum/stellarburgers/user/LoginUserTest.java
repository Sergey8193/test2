package praktikum.stellarburgers.user;

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

import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class LoginUserTest {
    SoftAssertions softAssertions;

    private UserClient userClient;
    private UserRegistrationData userRegistrationData;
    private String accessToken;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();
    }

    @After
    public void cleanUp() {
        if (!Objects.equals(accessToken, null)) {
            userClient.deleteUser(accessToken);
        }
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "loginUser")
    @Test
    @DisplayName("Login under existent 'User'")
    @Description("Check that registered 'User' can be authorized")
    public void loginUnderExistentUser() {
        ValidatableResponse createUserResponse = userClient.createUser(userRegistrationData);
        UserResponseBase createUserResponseBase = new UserResponseBase(createUserResponse);
        accessToken = createUserResponseBase.getAccessToken();

        ValidatableResponse response = userClient.loginUser(getCredentialsFrom(userRegistrationData));
        UserResponseBase userResponseBase = new UserResponseBase(response);

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();
        final String ACTUAL_NAME = userResponseBase.getName();
        final String ACTUAL_EMAIL = userResponseBase.getEmail();
        final String ACTUAL_ACCESS_TOKEN = userResponseBase.getAccessToken();
        final String ACTUAL_REFRESH_TOKEN = userResponseBase.getRefreshToken();

        final boolean EXPECTED_SUCCESS = true;
        final String EXPECTED_NAME = userRegistrationData.getName();
        final String EXPECTED_EMAIL = userRegistrationData.getEmail();

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_OK);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(null);
        softAssertions.assertAll();

        if (Objects.equals(ACTUAL_STATUS_CODE, SC_OK)) {
            accessToken = userResponseBase.getAccessToken();

            softAssertions.assertThat(ACTUAL_ACCESS_TOKEN).isNotEmpty();
            softAssertions.assertThat(ACTUAL_REFRESH_TOKEN).isNotEmpty();
            softAssertions.assertThat(ACTUAL_NAME).isEqualTo(EXPECTED_NAME);
            softAssertions.assertThat(ACTUAL_EMAIL).isEqualTo(EXPECTED_EMAIL);
            softAssertions.assertAll();
        }
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "loginUser")
    @Test
    @DisplayName("Login with incorrect 'User' data")
    @Description("Check that login with incorrect 'Email' and 'Password' is impossible")
    public void loginWithIncorrectEmailAndPassword() {
        String WRONG_EMAIL = userRegistrationData.getPassword();
        String WRONG_PASSWORD = userRegistrationData.getPassword().substring(0, 4);

        ValidatableResponse response = userClient.loginUser(new UserCredentials(WRONG_EMAIL, WRONG_PASSWORD));
        UserResponseBase userResponseBase = new UserResponseBase(response);

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();

        final boolean EXPECTED_SUCCESS = false;
        final String EXPECTED_MESSAGE = "email or password are incorrect";

        if (ACTUAL_STATUS_CODE == SC_OK) { accessToken = userResponseBase.getAccessToken(); }

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_UNAUTHORIZED);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
