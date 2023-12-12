package praktikum.stellarburgers.user;

import io.restassured.response.ValidatableResponse;
import lombok.Getter;
import lombok.Setter;
import praktikum.stellarburgers.user.UserFailureInfo;
import praktikum.stellarburgers.user.UserSuccessInfo;

import static org.apache.http.HttpStatus.SC_OK;

@Getter
@Setter
public class UserResponseBase {
    Integer code;
    Boolean success;
    String message;
    String accessToken;
    String refreshToken;
    String name;
    String email;

    public UserResponseBase(ValidatableResponse response) {
        this.code = response.extract().statusCode();
        boolean success;
        String message;

        if (this.code == SC_OK) {
            UserSuccessInfo userSuccessInfo = response
                    .extract()
                    .body()
                    .as(UserSuccessInfo.class);
            success = userSuccessInfo.isSuccess();
            message = null;
            this.accessToken = userSuccessInfo.getAccessToken();
            this.refreshToken = userSuccessInfo.getRefreshToken();
            this.name =  userSuccessInfo.getUser().getName();
            this.email =  userSuccessInfo.getUser().getEmail();

        } else {
            UserFailureInfo userFailureInfo = response
                    .extract()
                    .body()
                    .as(UserFailureInfo.class);
            success = userFailureInfo.isSuccess();
            message = userFailureInfo.getMessage();
            this.accessToken = null;
            this.refreshToken = null;
        }
        this.success = success;
        this.message = message;
    }
}
