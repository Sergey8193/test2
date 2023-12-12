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

public class ModifyUserTest {
    SoftAssertions softAssertions;

    private UserClient userClient;
    private UserRegistrationData userRegistrationData;
    private UserSuccessInfo userSuccessInfo;

    private String refreshToken;
    private String accessToken;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();

        userClient.createUser(userRegistrationData);
        userSuccessInfo = userClient
                .loginUser(getCredentialsFrom(userRegistrationData))
                .extract()
                .body()
                .as(UserSuccessInfo.class);
        accessToken = userSuccessInfo.getAccessToken();
        refreshToken = userSuccessInfo.getRefreshToken();
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
    @DisplayName("Modify logged in User data with correct new data")
    @Description("Check that User data can be modified")
    public void modifyLoggedInUserDataWithCorrectNewData() {
        UserRegistrationData newRegistrationData = getRandomUserRegistrationData();

        ValidatableResponse response = userClient.modifyUser(newRegistrationData, accessToken);
        final int ACTUAL_STATUS_CODE = response.extract().statusCode();
        boolean success;
        String message;

        if (ACTUAL_STATUS_CODE == SC_OK) {
            UserSuccessInfo userSuccessInfo = response
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            success = userSuccessInfo.isSuccess();
            message = null;
        } else {
            UserFailureInfo userFailureInfo = response
                    .extract()
                    .body()
                    .as(UserFailureInfo.class);
            success = userFailureInfo.isSuccess();
            message = userFailureInfo.getMessage();
        }
        final boolean ACTUAL_SUCCESS = success;
        final String ACTUAL_MESSAGE = message;

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_OK);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(true);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(null);
        softAssertions.assertAll();
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "modifyUser")
    @Test
    @DisplayName("Modify logged in User with existent user email")
    @Description("Check that User can not use someone else's email")
    public void modifyLoggedInUserWithExistentUserEmail() {
        UserRegistrationData someonesRegistrationData = getRandomUserRegistrationData();
        UserSuccessInfo someonesInfo = userClient
                .createUser(someonesRegistrationData)
                .extract()
                .body()
                .as(UserSuccessInfo.class);
        String someonesAccessToken = someonesInfo.getAccessToken();

        UserRegistrationData newRegistrationData = getRandomUserRegistrationData();
        newRegistrationData.setEmail(someonesInfo.getUser().getEmail());
        ValidatableResponse response = userClient.modifyUser(newRegistrationData, accessToken);
        final int ACTUAL_STATUS_CODE = response.extract().statusCode();
        boolean success;
        String message;

        if (ACTUAL_STATUS_CODE == SC_OK) {
            UserSuccessInfo userSuccessInfo = response
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            success = userSuccessInfo.isSuccess();
            message = null;
        } else {
            UserFailureInfo userFailureInfo = response
                    .extract()
                    .body()
                    .as(UserFailureInfo.class);
            success = userFailureInfo.isSuccess();
            message = userFailureInfo.getMessage();
        }
        final boolean ACTUAL_SUCCESS = success;
        final String ACTUAL_MESSAGE = message;
        final String EXPECTED_MESSAGE = "User with such email already exists";

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_FORBIDDEN);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(false);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();

        userClient.deleteUser(someonesAccessToken);
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "modifyUser")
    @Test
    @DisplayName("Modify User without authorization")
    @Description("Check that anonymous User can not use modifyUser endpoint")
    public void modifyUserWithoutAuthorization() {
        UserRegistrationData newRegistrationData = getRandomUserRegistrationData();
        newRegistrationData.setEmail(userRegistrationData.getEmail());
        ValidatableResponse response = userClient.modifyUser(userRegistrationData, null);
        final int ACTUAL_STATUS_CODE = response.extract().statusCode();
        boolean success;
        String message;

        if (ACTUAL_STATUS_CODE == SC_OK) {
            UserSuccessInfo userSuccessInfo = response
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            success = userSuccessInfo.isSuccess();
            message = null;
        } else {
            UserFailureInfo userFailureInfo = response
                    .extract()
                    .body()
                    .as(UserFailureInfo.class);
            success = userFailureInfo.isSuccess();
            message = userFailureInfo.getMessage();
        }
        final boolean ACTUAL_SUCCESS = success;
        final String ACTUAL_MESSAGE = message;
        final String EXPECTED_MESSAGE = "You should be authorised";

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_UNAUTHORIZED);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(false);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
