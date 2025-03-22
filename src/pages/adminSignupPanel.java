package pages;
import database.DatabaseConnection;
import admin.AdminDashboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class adminSignupPanel extends JPanel {
    public adminSignupPanel() {
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!");
                    return;
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO admin (username, email, password) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, password);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Admin Registered Successfully!");
                    new AdminDashboard();  // Open dashboard
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Signup Failed: " + ex.getMessage());
                }
            }
        });

        add(new JLabel(""));
        add(signupButton);
    }
}
