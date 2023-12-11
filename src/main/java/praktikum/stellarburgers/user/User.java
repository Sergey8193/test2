package praktikum.stellarburgers.user;

public class User extends UserContactInfo {
    private String createdAt;
    private String updatedAt;

    public String getName() { return super.getName(); }
    public void setName(String name) { super.setName(name); }

    public String getEmail() { return super.getEmail(); }
    public void setEmail(String email) { super.setEmail(email); }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "( name: '" + getName() +
                "', email: '" + getEmail() +
                "', createdAt: '" +createdAt +
                "', updatedAt: '" + updatedAt + "' )";
    }
}
