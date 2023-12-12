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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class CreateUserTest {
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
    @Story(value = "createUser")
    @Test
    @DisplayName("Create 'Unique user'")
    @Description("Check that 'Unique user' can be created")
    public void createUniqueUser() {
        ValidatableResponse response = userClient.createUser(userRegistrationData);
        UserResponseBase userResponseBase = new UserResponseBase(response);
        accessToken = userResponseBase.getAccessToken();

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();
        final String ACTUAL_NAME = userResponseBase.getName();
        final String ACTUAL_EMAIL = userResponseBase.getEmail();
        final String ACTUAL_ACCESS_TOKEN = userResponseBase.getAccessToken();
        final String ACTUAL_REFRESH_TOKEN = userResponseBase.getRefreshToken();

        final String EXPECTED_NAME = userRegistrationData.getName();
        final String EXPECTED_EMAIL = userRegistrationData.getEmail();

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_OK);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(true);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(null);
        softAssertions.assertThat(ACTUAL_ACCESS_TOKEN).isNotEmpty();
        softAssertions.assertThat(ACTUAL_REFRESH_TOKEN).isNotEmpty();
        softAssertions.assertThat(ACTUAL_NAME).isEqualTo(EXPECTED_NAME);
        softAssertions.assertThat(ACTUAL_EMAIL).isEqualTo(EXPECTED_EMAIL);
        softAssertions.assertAll();
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "createUser")
    @Test
    @DisplayName(" Create 'User' who is already registered")
    @Description("Check that 'User' with registered email can not be created")
    public void createUserWhoIsAlreadyRegistered() {
        ValidatableResponse someonesResponse = userClient.createUser(userRegistrationData);
        UserResponseBase someonesResponseBase = new UserResponseBase(someonesResponse);
        accessToken = someonesResponseBase.getAccessToken();

        ValidatableResponse response = userClient.createUser(userRegistrationData);
        UserResponseBase userResponseBase = new UserResponseBase(response);
        String userAccessToken = userResponseBase.getAccessToken();

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();

        final String EXPECTED_MESSAGE = "User already exists";

        if (!Objects.equals(userAccessToken, null)) { userClient.deleteUser(userAccessToken);  }

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_FORBIDDEN);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(false);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "createUser")
    @Test
    @DisplayName("Create 'User' without filling in any of required fields")
    @Description("Check that 'User' without 'Email' and 'Password' can not be created")
    public void createUserWithoutFillingInAnyOfRequiredFields() {
        userRegistrationData.setName("");
        userRegistrationData.setEmail("");
        userRegistrationData.setPassword("");

        ValidatableResponse response = userClient.createUser(userRegistrationData);
        UserResponseBase userResponseBase = new UserResponseBase(response);
        accessToken = userResponseBase.getAccessToken();

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();

        final String EXPECTED_MESSAGE = "Email, password and name are required fields";

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(SC_FORBIDDEN);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(false);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
