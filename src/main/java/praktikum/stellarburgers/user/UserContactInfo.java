package praktikum.stellarburgers.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContactInfo {
    private String name;
    private String email;

    public UserContactInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserContactInfo() { }

    @Override
    public String toString() {
        return "( name: '" + name +
                "', email: '" + email + "' )";
    }
}
