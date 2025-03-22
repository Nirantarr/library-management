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

public class adminLoginPanel extends JPanel {
    public adminLoginPanel() {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Both fields are required!");
                    return;
                }

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "SELECT * FROM admin WHERE email = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        new AdminDashboard();  // Open dashboard
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Login Failed: " + ex.getMessage());
                }
            }
        });

        add(new JLabel(""));
        add(loginButton);
    }
}
