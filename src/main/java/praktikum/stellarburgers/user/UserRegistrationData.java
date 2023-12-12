package praktikum.stellarburgers.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationData extends UserCredentials {
    private String name;

    public UserRegistrationData(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    @Override
    public String toString() {
        return "( email: '" + getEmail() +
                "', password: '" + getPassword() +
                "', name: '" + name + "' )";
    }
}
