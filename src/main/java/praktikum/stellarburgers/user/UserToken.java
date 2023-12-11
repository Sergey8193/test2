package praktikum.stellarburgers.user;

public class UserToken {

    private String token;

    public UserToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "( " + "token: '" + token + "' )";
    }
}
