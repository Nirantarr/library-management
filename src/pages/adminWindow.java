package pages;
import javax.swing.*;
import java.awt.*;

public class adminWindow {
    public adminWindow() {
        JFrame frame = new JFrame("Admin Portal");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(2, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Signup", new adminSignupPanel());
        tabbedPane.addTab("Login", new adminLoginPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
