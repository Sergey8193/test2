package praktikum.stellarburgers.user;

public class UserSuccessInfo {
    private boolean success;
    private UserContactInfo user;
    private String accessToken;
    private String refreshToken;


    public boolean isSuccess() { return success;  }
    public void setSuccess(boolean success) { this.success = success; }

    public UserContactInfo getUser() { return user; }
    public void setUser(UserContactInfo user) { this.user = user; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    @Override
    public String toString() {
        return "( success: '" + success +
                "', user: '" + user +
                "', accessToken: '" + accessToken +
                "', refreshToken: '" + refreshToken + "' )";
    }
}
