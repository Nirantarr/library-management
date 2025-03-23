package admin;
import database.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.*;

public class AddBook extends JFrame {
    private JTextField titleField, authorField,genreField,stockField;
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

        add(new JLabel("Genre:"));
        genreField = new JTextField();
        add(genreField);

        add(new JLabel("stock:"));
        stockField = new JTextField();
        add(stockField);

        addButton = new JButton("Add Book");
        add(addButton);

        addButton.addActionListener(e -> addBookToDB());

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addBookToDB() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO books (title, author, genre, stock) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, titleField.getText());  // Set book title
            pst.setString(2, authorField.getText()); // Set author name
            pst.setString(3, genreField.getText());  // Set book genre
            pst.setInt(4, Integer.parseInt(stockField.getText())); // Set stock quantity

            pst.executeUpdate(); // Execute the query
            JOptionPane.showMessageDialog(this, "Book Added Successfully");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock must be a number!", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

}
