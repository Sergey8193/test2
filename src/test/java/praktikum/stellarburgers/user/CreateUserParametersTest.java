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

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static praktikum.stellarburgers.user.UserDataGenerator.*;

@RunWith(Parameterized.class)
public class CreateUserParametersTest {
    SoftAssertions softAssertions;

    private UserClient userClient;
    private UserRegistrationData userRegistrationData;
    private String accessToken;

    private final String NAME;
    private final String EMAIL;
    private final String PASSWORD;
    private final int EXPECTED_STATUS_CODE;
    private final boolean EXPECTED_SUCCESS;
    private final String EXPECTED_MESSAGE;

    public CreateUserParametersTest(String name, String email, String password,
                                    int statusCode, boolean success, String message) {
        this.NAME = name;
        this.EMAIL = email;
        this.PASSWORD = password;
        this.EXPECTED_STATUS_CODE =statusCode;
        this.EXPECTED_SUCCESS = success;
        this.EXPECTED_MESSAGE =message;
    }

    @Parameterized.Parameters(name = "createUser ( name: {0}, email: {1}, password: {2} )")
    public static Object[][] getTestData() {
        return new Object[][]{
                { getRandomName(), getRandomEmail(), getRandomPassword(), SC_OK, true, null },
                { getRandomName(), getRandomEmail(), "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), getRandomEmail(), null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), "", getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), "", "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), "", null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), null, getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), null, "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { getRandomName(), null, null, SC_FORBIDDEN, false, "Email, password and name are required fields" },

                { "", getRandomEmail(), getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", getRandomEmail(), "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", getRandomEmail(), null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", "", getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", "", "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", "", null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", null, getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", null, "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { "", null, null, SC_FORBIDDEN, false, "Email, password and name are required fields" },

                { null, getRandomEmail(), getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, getRandomEmail(), "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, getRandomEmail(), null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, "", getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, "", "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, "", null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, null, getRandomPassword(), SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, null, "", SC_FORBIDDEN, false, "Email, password and name are required fields" },
                { null, null, null, SC_FORBIDDEN, false, "Email, password and name are required fields" },
        };
    }

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
        userClient = new UserClient();
        userRegistrationData = new UserRegistrationData(EMAIL, PASSWORD, NAME);
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
    @DisplayName("Create 'User'")
    @Description("Check that 'User' can be created only with all required parameters")
    public void createUser() {
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

        softAssertions.assertThat(ACTUAL_STATUS_CODE).isEqualTo(EXPECTED_STATUS_CODE);
        softAssertions.assertThat(ACTUAL_SUCCESS).isEqualTo(EXPECTED_SUCCESS);
        softAssertions.assertThat(ACTUAL_MESSAGE).isEqualTo(EXPECTED_MESSAGE);
        softAssertions.assertAll();

        if (Objects.equals(ACTUAL_STATUS_CODE, SC_OK)) {
            softAssertions.assertThat(ACTUAL_ACCESS_TOKEN).isNotEmpty();
            softAssertions.assertThat(ACTUAL_REFRESH_TOKEN).isNotEmpty();
            softAssertions.assertThat(ACTUAL_NAME).isEqualTo(NAME);
            softAssertions.assertThat(ACTUAL_EMAIL).isEqualTo(EMAIL);
            softAssertions.assertAll();
        }
    }
}
