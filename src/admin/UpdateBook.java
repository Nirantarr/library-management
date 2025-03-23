package admin;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateBook extends JPanel {
    private JTextField idField, titleField, authorField, genreField, stockField;
    private JButton searchButton, updateButton;

    public UpdateBook() {
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Enter Book ID:"));
        idField = new JTextField();
        add(idField);

        searchButton = new JButton("Search");
        add(new JLabel("")); // Empty placeholder
        add(searchButton);

        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField();
        add(authorField);

        add(new JLabel("Genre:"));
        genreField = new JTextField();
        add(genreField);

        add(new JLabel("Stock:"));
        stockField = new JTextField();
        add(stockField);

        updateButton = new JButton("Update Book");
        add(new JLabel("")); // Empty placeholder
        add(updateButton);

        // Initially, disable input fields until a book is found
        setFieldsEnabled(false);

        // Search Button Action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

        // Update Button Action
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });
    }

    private void setFieldsEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        authorField.setEnabled(enabled);
        genreField.setEnabled(enabled);
        stockField.setEnabled(enabled);
        updateButton.setEnabled(enabled);
    }

    private void searchBook() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                titleField.setText(rs.getString("title"));
                authorField.setText(rs.getString("author"));
                genreField.setText(rs.getString("genre"));
                stockField.setText(String.valueOf(rs.getInt("stock")));
                setFieldsEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "No book found with this ID!");
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void updateBook() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE books SET title = ?, author = ?, genre = ?, stock = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titleField.getText());
            stmt.setString(2, authorField.getText());
            stmt.setString(3, genreField.getText());
            stmt.setInt(4, Integer.parseInt(stockField.getText()));
            stmt.setInt(5, Integer.parseInt(idField.getText()));

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Book updated successfully!");
                setFieldsEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Book may not exist.");
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
