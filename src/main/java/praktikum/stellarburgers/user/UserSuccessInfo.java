package praktikum.stellarburgers.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSuccessInfo {
    private boolean success;
    private UserContactInfo user;
    private String accessToken;
    private String refreshToken;

    @Override
    public String toString() {
        return "( success: '" + success +
                "', user: '" + user +
                "', accessToken: '" + accessToken +
                "', refreshToken: '" + refreshToken + "' )";
    }
}
