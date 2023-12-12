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
    private String accessToken;

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

        if (Objects.equals(EMAIL, EXISTENT_EMAIL) || Objects.equals(PASSWORD, EXISTENT_PASSWORD)) {
            ValidatableResponse response = userClient.createUser(userRegistrationData);
            UserResponseBase userResponseBase = new UserResponseBase(response);
            accessToken = userResponseBase.getAccessToken();
        }
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
    @DisplayName("Login User parameters validation")
    @Description("Check that login is possible with existent 'Login' and 'Password' only")
    public void loginUserParametersValidation() {
        String email = Objects.equals(EMAIL, EXISTENT_EMAIL) ? userRegistrationData.getEmail() : EMAIL;
        String password = Objects.equals(PASSWORD, EXISTENT_PASSWORD) ? userRegistrationData.getPassword() : PASSWORD;

        ValidatableResponse response = userClient.loginUser(new UserCredentials(email, password));
        UserResponseBase userResponseBase = new UserResponseBase(response);

        final int ACTUAL_STATUS_CODE = userResponseBase.getCode();
        final boolean ACTUAL_SUCCESS = userResponseBase.getSuccess();
        final String ACTUAL_MESSAGE = userResponseBase.getMessage();

        if (ACTUAL_STATUS_CODE == SC_OK) {
            String newAccessToken = userResponseBase.getAccessToken();
            if (!Objects.equals(newAccessToken, null) && !Objects.equals(newAccessToken, accessToken)) {
                userClient.deleteUser(newAccessToken);
            }
        }

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();
    }
}
