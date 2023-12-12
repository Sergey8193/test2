package praktikum.stellarburgers.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends UserContactInfo {
    private String createdAt;
    private String updatedAt;

    @Override
    public String toString() {
        return "( name: '" + getName() +
                "', email: '" + getEmail() +
                "', createdAt: '" +createdAt +
                "', updatedAt: '" + updatedAt + "' )";
    }
}
