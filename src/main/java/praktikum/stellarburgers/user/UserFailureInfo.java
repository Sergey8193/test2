package praktikum.stellarburgers.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFailureInfo {
    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "( success: '" + success +
                "', message: '" + message + "' )";
    }
}
