package praktikum.stellarburgers.ingredient;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.stellarburgers.user.UserSuccessInfo;
import praktikum.stellarburgers.user.UserClient;
import praktikum.stellarburgers.user.UserRegistrationData;

import java.util.Objects;

import static org.apache.http.HttpStatus.SC_OK;
import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class GetIngredientsTest {
    IngredientClient ingredientClient;
    UserClient userClient;
    UserRegistrationData userRegistrationData;
    UserSuccessInfo userSuccessInfo;

    @Before
    public void setUp(){
        ingredientClient = new IngredientClient();
        userClient = new UserClient();
        userRegistrationData = getRandomUserRegistrationData();
        userClient.createUser(userRegistrationData);
        userSuccessInfo = userClient
                .loginUser(getCredentialsFrom(userRegistrationData))
                .extract()
                .body()
                .as(UserSuccessInfo.class);
    }

    @After
    public void cleanUp() {
        if (!Objects.equals(userSuccessInfo, null)) {
            if (!Objects.equals(userSuccessInfo.getAccessToken(), null) &&
                    (!userSuccessInfo.getAccessToken().isEmpty())) {
                userClient.logoutUser(userSuccessInfo.getRefreshToken());
                userClient.deleteUser(userSuccessInfo.getAccessToken());
            }
        }
    }

    @Epic(value = "Ingredient Client")
    @Feature(value = "operations")
    @Story(value = "getIngredients")
    @Test
    @DisplayName("'Get ingredients' request returns 'success' for logged in user")
    @Description("Check that if 'ingredients list' can be received by logged in user")
    public void getIngredientsRequestReturnsSuccessForLoggedInUser() {
        ValidatableResponse response = ingredientClient.getIngredients(userSuccessInfo.getAccessToken());
        response
                .assertThat()
                .statusCode(SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("data", Matchers.notNullValue())
                .and().body("data.size()", Matchers.greaterThan(0));
    }

    @Epic(value = "Ingredient Client")
    @Feature(value = "operations")
    @Story(value = "getIngredients")
    @Test
    @DisplayName("'Get ingredients' request returns 'success' for anonymous user")
    @Description("Check that if 'ingredients list' can be received by anonymous user")
    public void getIngredientsRequestReturnsSuccessForAnonymousUser() {
        ValidatableResponse response = ingredientClient.getIngredients(null);
        response
                .assertThat()
                .statusCode(SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("data", Matchers.notNullValue())
                .and().body("data.size()", Matchers.greaterThan(0));
    }
}
