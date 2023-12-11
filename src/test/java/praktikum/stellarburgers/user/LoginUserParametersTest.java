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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static praktikum.stellarburgers.user.UserDataGenerator.*;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomPassword;

@RunWith(Parameterized.class)
public class LoginUserParametersTest {
    SoftAssertions softAssertions;

    private UserClient userClient;
    UserFailureInfo userFailureInfo;

    private final String EMAIL;
    private final String PASSWORD;
    private final int EXPECTED_STATUS_CODE;
    private final boolean EXPECTED_SUCCESS;
    private final String EXPECTED_MESSAGE;

    public LoginUserParametersTest(String email, String password,
                                    int statusCode, boolean success, String message) {
        this.EMAIL = email;
        this.PASSWORD = password;
        this.EXPECTED_STATUS_CODE =statusCode;
        this.EXPECTED_SUCCESS = success;
        this.EXPECTED_MESSAGE = message;
    }

    @Parameterized.Parameters(name = "'Login User' parameters test: ( email: {0}, password: {1} )")
    public static Object[][] getTestData() {
        return new Object[][]{
                { getRandomEmail(), getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { getRandomEmail(), "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { getRandomEmail(), null, SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", null, SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, null, SC_UNAUTHORIZED, false, "email or password are incorrect" },
        };
    }

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
    }

    @After
    public void cleanUp() {
    }

    @Epic(value = "User Client")
    @Feature(value = "operations")
    @Story(value = "loginUser")
    @Test
    @DisplayName("Login with incorrect user data")
    @Description("Login with incorrect 'Login' and 'Password'")
    public void loginWithIncorrectLoginAndPassword() {
        ValidatableResponse response = userClient.loginUser(new UserCredentials(EMAIL, PASSWORD));
        userFailureInfo = response
                .extract()
                .body()
                .as(UserFailureInfo.class);

        final int ACTUAL_STATUS_CODE = response.extract().statusCode();
        final boolean ACTUAL_SUCCESS = userFailureInfo.isSuccess();
        final String ACTUAL_MESSAGE = userFailureInfo.getMessage();

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
