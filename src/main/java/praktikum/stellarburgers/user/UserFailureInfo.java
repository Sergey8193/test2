package praktikum.stellarburgers.user;

public class UserFailureInfo {
    private boolean success;
    private String message;

    public boolean isSuccess() { return success;  }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setAccessToken(String message) { this.message = message; }

    @Override
    public String toString() {
        return "( success: '" + success +
                "', message: '" + message + "' )";
    }
}
