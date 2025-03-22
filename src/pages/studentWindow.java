package pages;
import javax.swing.*;
import java.awt.*;

public class studentWindow {
    public studentWindow() {
        JFrame frame = new JFrame("Student Portal");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(2, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Signup", new studentSignupPanel());
        tabbedPane.addTab("Login", new studentLoginPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
