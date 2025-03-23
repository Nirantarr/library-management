package pages;

import admin.AdminDashboard;
import database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

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

        JButton signupButton = new JButton("Sign-Up");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                // Debugging: Print values to check inputs
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection conn = DatabaseConnection.getConnection()) {
                    if (conn == null) {
                        JOptionPane.showMessageDialog(null, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check current database
                    PreparedStatement stmt = conn.prepareStatement("SELECT DATABASE();");
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        System.out.println("Connected to database: " + rs.getString(1));
                    }

                    String sql = "INSERT INTO admin (username, email, password) VALUES (?, ?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, email);
                    stmt.setString(3, password);
                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "Admin Registered Successfully!");
                        new AdminDashboard();
                    } else {
                        JOptionPane.showMessageDialog(null, "Signup Failed!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Signup Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                
            }
        });

        add(new JLabel("")); // Empty cell for spacing
        add(signupButton);
    }
}
