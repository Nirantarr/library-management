package admin;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBook extends JPanel {
    private JTextField idField;
    private JButton deleteButton;

    public DeleteBook() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("Enter Book ID to Delete:"));
        idField = new JTextField();
        add(idField);

        deleteButton = new JButton("Delete Book");
        add(new JLabel("")); // Empty placeholder
        add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
    }

    private void deleteBook() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idField.getText()));

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Book deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No book found with this ID!");
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
