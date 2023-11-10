package sigurimi_info;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

        tabbedPane.setForegroundAt(0, Color.decode("#164863")); 
        tabbedPane.setBackgroundAt(0, Color.decode("#DDF2FD")); 
        tabbedPane.setForegroundAt(1, Color.decode("#164863")); 
        tabbedPane.setBackgroundAt(1, Color.decode("#DDF2FD")); 

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
        panel.setBackground(Color.decode("#164863"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add padding

        panel.add(content, constraints);
        return panel;
    }

    private static JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#164863"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add padding
        constraints.anchor = GridBagConstraints.WEST;
    
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
    
        Font customFont = new Font("Arial", Font.BOLD, 13);
        usernameLabel.setFont(customFont);
        passwordLabel.setFont(customFont);
        registerButton.setFont(customFont);
        clearButton.setFont(customFont);

        usernameLabel.setForeground(Color.decode("#DDF2FD"));
        passwordLabel.setForeground(Color.decode("#DDF2FD"));
    
        // Set the same height for both usernameField and passwordField
        int fieldHeight = 25; // Adjust the height as needed
        usernameField.setPreferredSize(new Dimension(usernameField.getPreferredSize().width, fieldHeight));
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, fieldHeight));
    
        // Create a custom border with an initial underline color
        final Border[] underlinedBorder = {BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)};
    
        // Add focus listeners to change the border color on focus
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#87C4FF"));
                usernameField.setBorder(underlinedBorder[0]);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY); 
                usernameField.setBorder(underlinedBorder[0]);
            }
        });
    
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#87C4FF")); 
                passwordField.setBorder(underlinedBorder[0]);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY); 
                passwordField.setBorder(underlinedBorder[0]);
            }
        });
    
        // Set a fixed size for the buttons
        Dimension buttonDimension = new Dimension(89, 25); // Adjust the dimensions as needed
        registerButton.setPreferredSize(buttonDimension);
        clearButton.setPreferredSize(buttonDimension);
    
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(usernameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(passwordField, constraints);
    
        // Create a panel for buttons and add both buttons to it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#164863"));
        buttonPanel.add(registerButton);
    
        // Add space between the buttons using empty labels
        JLabel spaceLabel = new JLabel("   "); // Add more spaces as needed
        buttonPanel.add(spaceLabel);
    
        buttonPanel.add(clearButton);
    
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(buttonPanel, constraints);
    
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
    
                registerUser(username, password);
    
                JOptionPane.showMessageDialog(panel, "Registration successful.");
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add code to clear the fields here
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    
        return panel;
    }
    
    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#164863"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Add padding
        constraints.anchor = GridBagConstraints.WEST;
    
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton clearButton = new JButton("Clear");
    
        Font customFont = new Font("Arial", Font.BOLD, 13);
        usernameLabel.setFont(customFont);
        passwordLabel.setFont(customFont);
        loginButton.setFont(customFont);
        clearButton.setFont(customFont);

        usernameLabel.setForeground(Color.decode("#DDF2FD"));
        passwordLabel.setForeground(Color.decode("#DDF2FD"));
    
        // Set the same height for both usernameField and passwordField
        int fieldHeight = 25; // Adjust the height as needed
        usernameField.setPreferredSize(new Dimension(usernameField.getPreferredSize().width, fieldHeight));
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, fieldHeight));
    
        // Create a custom border with an initial underline color
        final Border[] underlinedBorder = {BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)};
    
        // Add focus listeners to change the border color on focus
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#87C4FF"));
                usernameField.setBorder(underlinedBorder[0]);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY); 
                usernameField.setBorder(underlinedBorder[0]);
            }
        });
    
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#87C4FF")); 
                passwordField.setBorder(underlinedBorder[0]);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                underlinedBorder[0] = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY); 
                passwordField.setBorder(underlinedBorder[0]);
            }
        });
    
        // Set a fixed size for the buttons
        Dimension buttonDimension = new Dimension(89, 25); // Adjust the dimensions as needed
        loginButton.setPreferredSize(buttonDimension);
        clearButton.setPreferredSize(buttonDimension);

        loginButton.setForeground(Color.decode("#164863"));
        loginButton.setBackground(Color.decode("#DDF2FD"));
        clearButton.setForeground(Color.decode("#164863"));
        clearButton.setBackground(Color.decode("#DDF2FD"));
    
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(usernameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(passwordField, constraints);
    
        // Create a panel for buttons and add both buttons to it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#164863"));
        buttonPanel.add(loginButton);
    
        // Add space between the buttons using empty labels
        JLabel spaceLabel = new JLabel("   "); // Add more spaces as needed
        buttonPanel.add(spaceLabel);
    
        buttonPanel.add(clearButton);
    
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        panel.add(buttonPanel, constraints);
    
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
    
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add code to clear the fields here
                usernameField.setText("");
                passwordField.setText("");
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
