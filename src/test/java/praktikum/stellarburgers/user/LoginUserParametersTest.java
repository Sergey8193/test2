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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static praktikum.stellarburgers.user.UserDataGenerator.*;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomPassword;

@RunWith(Parameterized.class)
public class LoginUserParametersTest {
    SoftAssertions softAssertions;

    private UserClient userClient;
    private UserRegistrationData userRegistrationData;
    private UserSuccessInfo userSuccessInfo;

    private static final String EXISTENT_EMAIL = "existent email";
    private static final String EXISTENT_PASSWORD = "existent password";

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

    @Parameterized.Parameters(name = "loginUser ( email: {0}, password: {1} )")
    public static Object[][] getTestData() {
        return new Object[][]{
                { EXISTENT_EMAIL, EXISTENT_PASSWORD, SC_OK, true, null },
                { EXISTENT_EMAIL, getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { EXISTENT_EMAIL, "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { EXISTENT_EMAIL, null, SC_UNAUTHORIZED, false, "email or password are incorrect" },

                { getRandomEmail(),  EXISTENT_PASSWORD, SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { getRandomEmail(), getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { getRandomEmail(), "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { getRandomEmail(), null, SC_UNAUTHORIZED, false, "email or password are incorrect" },

                { "", EXISTENT_PASSWORD, SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { "", null, SC_UNAUTHORIZED, false, "email or password are incorrect" },

                { null, EXISTENT_PASSWORD, SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, getRandomPassword(), SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, "", SC_UNAUTHORIZED, false, "email or password are incorrect" },
                { null, null, SC_UNAUTHORIZED, false, "email or password are incorrect" },
        };
    }

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
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
    @DisplayName("Login User parameters validation")
    @Description("Check that login is possible with existent 'Login' and 'Password' only")
    public void loginUserParametersValidation() {
        String email = Objects.equals(EMAIL, EXISTENT_EMAIL) ? userRegistrationData.getEmail() : EMAIL;
        String password = Objects.equals(PASSWORD, EXISTENT_PASSWORD) ? userRegistrationData.getPassword() : PASSWORD;

        ValidatableResponse response = userClient.loginUser(new UserCredentials(email, password));
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

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
