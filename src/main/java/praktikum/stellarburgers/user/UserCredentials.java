package praktikum.stellarburgers.user;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials getCredentialsFrom(UserRegistrationData user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    @Override
    public String toString() {
        return "( email: '" + email +
                "', password: '" + password + "' )";
    }
}
