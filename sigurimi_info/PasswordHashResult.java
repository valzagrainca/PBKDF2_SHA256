package sigurimi_info;

public class PasswordHashResult {
    private final String hash;
    private final String salt;

    public PasswordHashResult(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }
}
