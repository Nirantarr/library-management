package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.DatabaseConnection;

public class DeleteBook extends JFrame {
    private JTextField bookIdField;
    private JButton deleteButton;

    public DeleteBook() {
        setTitle("Delete Book");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel bookIdLabel = new JLabel("Enter Book ID:");
        bookIdLabel.setBounds(20, 30, 100, 25);
        add(bookIdLabel);

        bookIdField = new JTextField();
        bookIdField.setBounds(130, 30, 120, 25);
        add(bookIdField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(90, 80, 100, 30);
        add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        setVisible(true);
    }

    private void deleteBook() {
        String bookId = bookIdField.getText();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID cannot be empty.");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(bookId));

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }
}
