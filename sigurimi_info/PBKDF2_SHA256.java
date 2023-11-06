package sigurimi_info;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

class PBKDF2_SHA256{
    //method to  generate a PBKDF2 hash using SHA-256
    public static String generatePBKDF2Hash(String password, byte[] salt, int iterations, int keyLength) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength * 8);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
    //generate rendom salt
    public static byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }

    public static void main(String[] args) {
        String password = "your_password";
        int iterations = 10000; 
        int keyLength = 256; // Key length in bits
        byte[] salt = generateSalt(16); // Generate a random salt

        String hash = generatePBKDF2Hash(password, salt, iterations, keyLength);
        System.out.println("PBKDF2 Hash: " + hash);
        System.out.println("Salt: " + Base64.getEncoder().encodeToString(salt));
    }

}


