package admin;
import database.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.*;

public class AddBook extends JFrame {
    private JTextField titleField, authorField, yearField;
    private JButton addButton;

    public AddBook() {
        setTitle("Add Book");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField();
        add(authorField);

        add(new JLabel("Year:"));
        yearField = new JTextField();
        add(yearField);

        addButton = new JButton("Add Book");
        add(addButton);

        addButton.addActionListener(e -> addBookToDB());

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addBookToDB() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, titleField.getText());
            pst.setString(2, authorField.getText());
            pst.setInt(3, Integer.parseInt(yearField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book Added Successfully");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
