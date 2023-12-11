package praktikum.stellarburgers.user;

public class UserContactInfo {
    private String name;
    private String email;

    public UserContactInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserContactInfo() { }

    public static UserContactInfo getContactInfoFrom(UserRegistrationData user) {
        return new UserContactInfo(user.getEmail(), user.getName());
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "( name: '" + name +
                "', email: '" + email + "' )";
    }
}
