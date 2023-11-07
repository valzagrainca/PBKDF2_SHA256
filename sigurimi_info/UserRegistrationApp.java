package sigurimi_info;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class UserRegistrationApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("User Registration and Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        centerFrameOnScreen(frame);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Registration Panel
        JPanel registrationPanel = createCenteredPanel(createRegistrationPanel());
        tabbedPane.addTab("Registration", registrationPanel);

        // Login Panel
        JPanel loginPanel = createCenteredPanel(createLoginPanel());
        tabbedPane.addTab("Login", loginPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private static void centerFrameOnScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    private static JPanel createCenteredPanel(Component content) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // Add padding

        panel.add(content, constraints);
        return panel;
    }

    private static JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); 
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                registerUser(username, password);

                JOptionPane.showMessageDialog(panel, "Registration successful.");
            }
        });

        return panel;
    }

    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); 
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Call a method to validate the login
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(panel, "Login successful.");
                } else {
                    JOptionPane.showMessageDialog(panel, "Login failed. Please check your credentials.");
                }
            }
        });

        return panel;
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

    // Add a method to validate the login
    private static boolean validateLogin(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/InformationSecurity", "postgres", "1234");
            String selectQuery = "SELECT username, password_hash, password_salt FROM users_credentials WHERE username = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password_hash");
                String storedSalt = resultSet.getString("password_salt");

                // Check if the entered password matches the stored hash and salt
                String enteredHash = PBKDF2_SHA256.generatePBKDF2Hash(password, Base64.getDecoder().decode(storedSalt), 10000, 256);
                if (storedHash.equals(enteredHash)) {
                    return true;
                }
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
