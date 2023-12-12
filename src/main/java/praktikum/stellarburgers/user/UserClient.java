package praktikum.stellarburgers.user;

import praktikum.stellarburgers.constants.RestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_AUTH_URL = BASE_URL + "auth/";

    @Step("POST createUser ( userRegistrationData: {userRegistrationData} )")
    public ValidatableResponse createUser(UserRegistrationData userRegistrationData) {
        return given()
                .spec(getRequestSpecification())
                .body(userRegistrationData)
                .when()
                .post(USER_AUTH_URL + "register/")
                .then();
    }

    @Step("POST loginUser ( userData: {userData} )")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getRequestSpecification())
                .body(userCredentials)
                .when()
                .post(USER_AUTH_URL + "login/")
                .then();
    }

    @Step("PATCH modifyUser ( userRegistrationData: {userRegistrationData}, accessToken: \"{accessToken}\" )")
    public ValidatableResponse modifyUser(UserRegistrationData userRegistrationData, String accessToken) {
        return given()
                .spec(getRequestSpecification(accessToken))
                .body(userRegistrationData)
                .when()
                .patch(USER_AUTH_URL + "user/")
                .then();
    }

    @Step("POST logoutUser ( refreshToken: \"{refreshToken}\" )")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(getRequestSpecification())
                .body(refreshToken)
                .when()
                .post(USER_AUTH_URL + "logout/")
                .then();
    }

    @Step("DELETE deleteUser ( accessToken: \"{accessToken}\" )")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getRequestSpecification(accessToken))
                .when()
                .delete(USER_AUTH_URL + "user/")
                .then();
    }
}
