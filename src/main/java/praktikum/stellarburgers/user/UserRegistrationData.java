package praktikum.stellarburgers.user;

public class UserRegistrationData extends UserCredentials {
    private String name;

    public UserRegistrationData(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    public String getEmail() { return super.getEmail(); }
    public void setEmail(String login) { super.setEmail(login); }

    public String getPassword() { return super.getPassword(); }
    public void setPassword(String password) {
        super.setPassword(password);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "( email: '" + getEmail() +
                "', password: '" + getPassword() +
                "', name: '" + name + "' )";
    }
}
