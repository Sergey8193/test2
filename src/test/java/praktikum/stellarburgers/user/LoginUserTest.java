package praktikum.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
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
    private UserSuccessInfo userSuccessInfo;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();
        ValidatableResponse response = userClient.createUser(userRegistrationData);
        userSuccessInfo = response.extract().body().as(UserSuccessInfo.class);
    }

    @After
    public void cleanUp() {
        if (!Objects.equals(userSuccessInfo, null)) {
            if (!Objects.equals(userSuccessInfo.getAccessToken(), null) &&
                    (!userSuccessInfo.getAccessToken().isEmpty())) {
                userClient.deleteUser(userSuccessInfo.getAccessToken());
            }
        }
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "loginUser")
    @Test
    @DisplayName("Login under existent 'User'")
    @Description("Check that 'User' can be authorized")
    public void loginUnderExistingUser() {
        ValidatableResponse response = userClient.loginUser(getCredentialsFrom(userRegistrationData));
        userSuccessInfo = response.extract().body().as(UserSuccessInfo.class);

        final int ACTUAL_STATUS_CODE = response.extract().statusCode();

        final String ACTUAL_NAME = userSuccessInfo.getUser().getName();
        final String ACTUAL_EMAIL = userSuccessInfo.getUser().getEmail();
        final String ACTUAL_ACCESS_TOKEN = userSuccessInfo.getAccessToken();
        final String ACTUAL_REFRESH_TOKEN = userSuccessInfo.getRefreshToken();

        final String EXPECTED_NAME = userRegistrationData.getName();
        final String EXPECTED_EMAIL = userRegistrationData.getEmail();

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_OK);
        softAssertions.assertThat(ACTUAL_ACCESS_TOKEN).isNotEmpty();
        softAssertions.assertThat(ACTUAL_REFRESH_TOKEN).isNotEmpty();
        softAssertions.assertThat(ACTUAL_NAME).isEqualTo(EXPECTED_NAME);
        softAssertions.assertThat(ACTUAL_EMAIL).isEqualTo(EXPECTED_EMAIL);
        softAssertions.assertAll();
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "loginUser")
    @Test
    @DisplayName("Login with incorrect 'User' data")
    @Description("Login with incorrect 'Login' and 'Password'")
    public void loginWithIncorrectLoginAndPassword() {
        String WRONG_EMAIL = userRegistrationData.getPassword();
        String WRONG_PASSWORD = userRegistrationData.getPassword().substring(0, 4);

        ValidatableResponse response = userClient.loginUser(new UserCredentials(WRONG_EMAIL, WRONG_PASSWORD));
        UserFailureInfo userFailureInfo = response
                .log().all()
                .extract()
                .body()
                .as(UserFailureInfo.class);

        final int ACTUAL_STATUS_CODE = response.extract().statusCode();
        final boolean ACTUAL_SUCCESS = userFailureInfo.isSuccess();
        final String ACTUAL_MESSAGE = userFailureInfo.getMessage();

        final boolean EXPECTED_SUCCESS = false;
        final String EXPECTED_MESSAGE = "email or password are incorrect";

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_UNAUTHORIZED);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
