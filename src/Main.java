import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import pages.adminWindow;
import pages.studentWindow;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(2, 1));

        JButton adminButton = new JButton("Admin");
        JButton studentButton = new JButton("Student");

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new adminWindow();
            }
        });

        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new studentWindow();
            }
        });

        frame.add(adminButton);
        frame.add(studentButton);

        frame.setVisible(true);
    }
}
