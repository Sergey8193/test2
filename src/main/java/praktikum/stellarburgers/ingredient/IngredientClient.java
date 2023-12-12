package praktikum.stellarburgers.ingredient;

import praktikum.stellarburgers.constants.RestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientClient extends RestClient {

    @Step("GET getIngredients ( accessToken: \"{accessToken}\" )")
    public ValidatableResponse getIngredients(String accessToken) {
        return given()
                .spec(getRequestSpecification(accessToken))
                .when()
                .get("/ingredients")
                .then();
    }
}
