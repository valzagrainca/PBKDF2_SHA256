package sigurimi_info;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

public class UserRegistrationApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        frame.add(panel);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Call a method to hash and store the password in the database
                registerUser(username, password);

                // Optionally, display a success message or navigate to another page
                JOptionPane.showMessageDialog(frame, "Registration successful.");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private static void registerUser(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/InformationSecurity", "postgres", "1234");
            String insertQuery = "INSERT INTO users_credentials (username, password_hash, password_salt) VALUES (?, ?, ?)";
            
            PasswordHashResult hashResult = hashPassword(password);

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashResult.getHash());
            preparedStatement.setString(3, hashResult.getSalt());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static PasswordHashResult hashPassword(String password) {
        int iterations = 10000;
        int keyLength = 256;
        byte[] salt = PBKDF2_SHA256.generateSalt(16);

        String hash = PBKDF2_SHA256.generatePBKDF2Hash(password, salt, iterations, keyLength);
        return new PasswordHashResult(hash, Base64.getEncoder().encodeToString(salt));
    }
}
