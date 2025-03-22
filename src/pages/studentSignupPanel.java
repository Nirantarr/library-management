package pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentSignupPanel extends JPanel {
    public studentSignupPanel() {
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        JButton signupButton = new JButton("Sign Up");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Student Signed Up!");
            }
        });

        add(new JLabel(""));
        add(signupButton);
    }
}
