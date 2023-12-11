package praktikum.stellarburgers.user;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
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
    private UserSuccessInfo userSuccessInfo;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();
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
    @Story(value = "createUser")
    @Test
    @DisplayName("Create 'Unique user'")
    @Description("Check that 'Unique user' can be created")
    public void createUniqueUser() {
        ValidatableResponse response = userClient.createUser(userRegistrationData);
        response
                .assertThat()
                .statusCode(SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());

        userSuccessInfo = response.extract().body().as(UserSuccessInfo.class);

        final String EXPECTED_NAME = userRegistrationData.getName();
        final String EXPECTED_EMAIL = userRegistrationData.getEmail();

        final String ACTUAL_NAME = userSuccessInfo.getUser().getName();
        final String ACTUAL_EMAIL = userSuccessInfo.getUser().getEmail();

        final String ACTUAL_ACCESS_TOKEN = userSuccessInfo.getAccessToken();
        final String ACTUAL_REFRESH_TOKEN = userSuccessInfo.getRefreshToken();

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
        userClient.createUser(userRegistrationData);
        ValidatableResponse response = userClient.createUser(userRegistrationData);
        response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("User already exists"));
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
        response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("Email, password and name are required fields"));
    }
}
